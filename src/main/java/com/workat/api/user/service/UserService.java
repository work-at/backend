package com.workat.api.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.workat.api.auth.dto.response.AuthResponse;
import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.user.dto.ActivityTypeDto;
import com.workat.api.user.dto.SignUpResponse;
import com.workat.api.user.dto.request.EmailCertifyRequest;
import com.workat.api.user.dto.request.SignUpRequest;
import com.workat.api.user.dto.request.UserUpdateRequest;
import com.workat.api.user.dto.response.EmailLimitResponseDto;
import com.workat.api.user.dto.response.MyProfileResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.FileUploadException;
import com.workat.common.exception.ForbiddenException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.FileUploadUtils;
import com.workat.domain.auth.OauthType;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.map.repository.worker.WorkerLocationRedisRepository;
import com.workat.domain.user.activity.ActivityType;
import com.workat.domain.user.entity.UserActivity;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.filter.FilterEmail;
import com.workat.domain.user.repository.UserActivityRepository;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	@Value("${resources.upload-folder:/home/work_at_tour/images}")
	private String resourcesLocation;
	@Value("${resources.upload-uri:/uploaded}")
	private String uploadUri;

	private final JavaMailSender mailSender;

	private final UsersRepository userRepository;

	private final UserProfileRepository userProfileRepository;

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	private final UserActivityRepository userActivityRepository;

	private final AuthorizationService authorizationService;

	private final ChatRoomRepository chatRoomRepository;

	public AuthResponse login(OauthType oauthType, long oauthId) {
		final boolean userExist = validateUserExistWithOauthId(oauthType, oauthId);

		if (!userExist) {
			return AuthResponse.ResponseForSignup(oauthId);
		}
		final Users user = userRepository.findByOauthTypeAndOauthId(oauthType, oauthId).orElseThrow(() -> new NotFoundException("user not found"));
		final String accessToken = authorizationService.createAccessToken(user.getId());

		return AuthResponse.ResponseForLogin(accessToken);
	}

	@Transactional
	public SignUpResponse signUp(SignUpRequest signUpRequest) {
		final OauthType oauthType = signUpRequest.getOauthType();
		final long oauthId = signUpRequest.getOauthId();

		final boolean userExist = validateUserExistWithOauthId(oauthType, oauthId);

		if (userExist) {
			log.error("user already exists");

			throw new ConflictException("user already exists");
		}

		Users users = Users.of(signUpRequest.getOauthType(), signUpRequest.getOauthId());
		UserProfile userProfile = UserProfile.builder()
			.user(users)
			.nickname(signUpRequest.getNickname())
			.position(signUpRequest.getPosition())
			.workingYear(signUpRequest.getWorkingYear())
			.build();
		userProfileRepository.save(userProfile);

		final long id = users.getId();
		final String accessToken = authorizationService.createAccessToken(id);

		return SignUpResponse.of(accessToken);
	}

	@Transactional(readOnly = true)
	public Optional<Users> findUserWithId(long id) {
		// id 로 가입된 유저 있는지 확인
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public boolean isUserNicknameExists(String nickname) {
		return userProfileRepository.findFirstByNickname(nickname).isPresent();
	}

	@Transactional(readOnly = true)
	public boolean validateUserExistWithOauthId(OauthType oauthType, long oauthId) {
		return userRepository.findByOauthTypeAndOauthId(oauthType, oauthId).isPresent();
	}

	@Transactional(readOnly = true)
	public MyProfileResponse getSelfUserProfile(Long userId) {
		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		int workchats = chatRoomRepository.findAllByUser(userProfile.getUser()).size();
		List<ActivityTypeDto> activityTypes = userActivityRepository.findByUser_Id(userProfile.getId()).stream()
			.map(UserActivity::getActivity)
			.map(activity -> ActivityTypeDto.of(activity.name(), activity.getType()))
			.collect(Collectors.toList());

		return MyProfileResponse.of(userProfile, workchats, activityTypes);
	}

	@Transactional
	public void updateUserProfile(Users user, UserUpdateRequest request) {
		userProfileRepository.findFirstByNicknameAndIdNot(request.getNickname(), user.getId()).ifPresent(s -> {
			throw new ConflictException("동일한 닉네임의 유저가 존재합니다.");
		});

		UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		userProfile.updateProfile(request.getNickname(), request.getPosition(), request.getWorkingYear(), request.getStory());
		userProfileRepository.save(userProfile);

		List<UserActivity> activityTypes = request.getActivities().stream()
			.map(ActivityType::of)
			.map(activityType -> UserActivity.of(user, activityType))
			.collect(Collectors.toList());

		userActivityRepository.saveAll(activityTypes);
	}

	@Transactional
	public String uploadProfileImage(Long userId, MultipartFile multipartFile) {
		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));

		if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().isBlank()) {
			throw new BadRequestException("빈 파일입니다.");
		}
		String savedFileName = null;
		try {
			savedFileName = FileUploadUtils.fileUpload(resourcesLocation, String.valueOf(userId), multipartFile.getBytes());
			log.info("save actual path: " + savedFileName);

			savedFileName = savedFileName.replace(resourcesLocation, uploadUri);
			log.info("access image path: " + savedFileName);

			userProfile.updateImage(savedFileName);
			userProfileRepository.save(userProfile);
		} catch (Exception e) {
			throw new FileUploadException(e.getMessage());
		}
		return savedFileName;
	}

	@Transactional
	public void sendCompanyVerifyEmail(Users user, EmailCertifyRequest request, String siteURL) throws UnsupportedEncodingException, MessagingException {
		if (user.getEmailRequestRemain() == 0) {
			throw new ForbiddenException("email 인증 요청이 모두 소모되었습니다");
		}

		Arrays.stream(FilterEmail.values())
			.filter(filterEmail -> request.getEmail().endsWith(filterEmail.getEmail()))
			.findFirst()
			.orElseThrow(() -> new BadRequestException("기본 이메일은 인증할 수 없습니다"));

		UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("user not found"));
		user.decreaseEmailRequestRemain();
		user.setVerificationCode();
		userRepository.save(user);

		sendVerificationEmail(user, userProfile.getNickname(), request.getEmail(), siteURL);
	}

	@Transactional
	public boolean verify(String verificationCode, String address) {
		Users user = userRepository.findUsersByVerificationCode(verificationCode).orElseThrow(() -> new NotFoundException("verification code not found"));
		UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("user not found"));

		user.clearVerificationCode();
		userRepository.save(user);
		userProfile.certifyCompanyMail(address);
		userProfileRepository.save(userProfile);

		return true;
	}

	@Transactional
	public void changeUserTracking(Users user, boolean turnOff) {
		if (turnOff) {
			turnOffTracking(user);
			return;
		}
		turnOnTracking(user);
	}

	private void turnOffTracking(Users user) {
		workerLocationRedisRepository.deleteById(user.getId());

		user.turnOffTracking();
		userRepository.save(user);
	}

	private void turnOnTracking(Users user) {
		user.turnOnTracking();
		userRepository.save(user);
	}

	public EmailLimitResponseDto getVerificationEmailRemain(Users user) {
		return EmailLimitResponseDto.of(user.getEmailRequestRemain());
	}

	private void sendVerificationEmail(Users user, String nickname, String email, String siteURL) throws MessagingException, UnsupportedEncodingException {
		// TODO: 기획에 따라 메일 내용 변경
		String fromAddress = "workat.test.mail@gmail.com";
		String senderName = "Work at_";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
			+ "Please click the link below to verify your registration:<br>"
			+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
			+ "Thank you,<br>"
			+ "Your company name.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(email);
		helper.setSubject(subject);

		content = content.replace("[[name]]", nickname);
		String verifyURL = siteURL + "/api/v1/user/email-verified?code=" + user.getVerificationCode() + "&address=" + email;

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);
	}
}
