package com.workat.api.user.service;

import java.io.UnsupportedEncodingException;
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
import com.workat.common.exception.UserNotFoundException;
import com.workat.common.util.FileUploadUtils;
import com.workat.domain.auth.OauthType;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.map.repository.worker.WorkerLocationRedisRepository;
import com.workat.domain.user.activity.ActivityType;
import com.workat.domain.user.entity.UserActivity;
import com.workat.domain.user.entity.UserBlocking;
import com.workat.domain.user.entity.UserEmailLimit;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.filter.FilterEmail;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UserActivityRepository;
import com.workat.domain.user.repository.UserEmailLimitRepository;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;
import com.workat.domain.user.repository.blocking.UserBlockingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final JavaMailSender mailSender;
	private final UsersRepository userRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserBlockingRepository userBlockingRepository;
	private final WorkerLocationRedisRepository workerLocationRedisRepository;
	private final UserActivityRepository userActivityRepository;
	private final AuthorizationService authorizationService;
	private final ChatRoomRepository chatRoomRepository;
	private final UserEmailLimitRepository userEmailLimitRepository;

	@Value("${resources.upload-folder:/home/work_at_tour/images}")
	private String resourcesLocation;
	@Value("${resources.upload-uri:/uploaded}")
	private String uploadUri;
	@Value("${resources.profile-upload-folder:/profile_images}")
	private String profileFolder;
	@Value("${spring.mail.username}")
	private String fromAddress;

	public AuthResponse loginWithOauth(OauthType oauthType, long oauthId) {
		final boolean userExist = validateUserExistWithOauthId(oauthType, oauthId);

		if (!userExist) {
			return AuthResponse.ResponseForSignup(oauthId);
		}

		final Users user = userRepository.findByOauthTypeAndOauthId(oauthType, oauthId)
			.orElseThrow(() -> new NotFoundException("user not found"));

		final String accessToken = authorizationService.createAccessToken(user.getId());

		return AuthResponse.ResponseForLogin(accessToken);
	}

	@Transactional
	public AuthResponse loginForTest(long testId) {
		final Users user = userRepository.findByOauthTypeAndOauthId(OauthType.TEST, testId)
			.orElseGet(() -> saveTestUser(testId));

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
	public MyProfileResponse getSelfUserProfile(Users user, String baseUrl) {
		UserProfile userProfile = userProfileRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		int workchats = chatRoomRepository.findAllByUser(userProfile.getUser()).size();
		List<ActivityTypeDto> activityTypes = userActivityRepository.findByUser_Id(userProfile.getId()).stream()
			.map(UserActivity::getActivity)
			.map(activity -> ActivityTypeDto.of(activity.name(), activity.getType()))
			.collect(Collectors.toList());

		return MyProfileResponse.of(userProfile, workchats, activityTypes, user.isTrackingOff(), baseUrl);
	}

	@Transactional
	public void updateUserProfile(Users user, UserUpdateRequest request) {
		userProfileRepository.findFirstByNicknameAndIdNot(request.getNickname(), user.getId()).ifPresent(s -> {
			throw new ConflictException("동일한 닉네임의 유저가 존재합니다.");
		});

		UserProfile userProfile = userProfileRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		userProfile.updateProfile(request.getNickname(), request.getPosition(), request.getWorkingYear(),
			request.getStory());
		userProfileRepository.save(userProfile);

		List<UserActivity> activityTypes = request.getActivities().stream()
			.map(ActivityType::of)
			.map(activityType -> UserActivity.of(user, activityType))
			.collect(Collectors.toList());
		userActivityRepository.deleteByUser_Id(user.getId());
		userActivityRepository.saveAll(activityTypes);
	}

	@Transactional
	public String uploadProfileImage(Long userId, MultipartFile multipartFile) {
		UserProfile userProfile = userProfileRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));

		if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().isBlank()) {
			throw new BadRequestException("빈 파일입니다.");
		}
		String savedFileName = null;
		try {
			savedFileName = FileUploadUtils.fileUpload(resourcesLocation + profileFolder, String.valueOf(userId),
				multipartFile.getBytes());
			log.info("save actual path: " + savedFileName);

			savedFileName = savedFileName.replace(resourcesLocation + profileFolder, uploadUri + profileFolder);
			log.info("access image path: " + savedFileName);

			userProfile.updateImage(savedFileName);
			userProfileRepository.save(userProfile);
		} catch (Exception e) {
			throw new FileUploadException(e.getMessage());
		}
		return savedFileName;
	}

	@Transactional
	public void sendCompanyVerifyEmail(Users user, EmailCertifyRequest request, String siteURL, String userAgent) throws
		UnsupportedEncodingException,
		MessagingException {
		if (user.getEmailRequestRemain() == 0) {
			if (userEmailLimitRepository.existsById(user.getId())) {
				throw new ForbiddenException("email 인증 요청이 모두 소모되었습니다");
			}
			user.resetEmailRequestCount();
		}

		if (FilterEmail.anyMatch(request.getEmail())) {
			throw new BadRequestException("기본 이메일 " + request.getEmail() + " 은 인증할 수 없습니다");
		}

		UserProfile userProfile = userProfileRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("user not found"));
		user.decreaseEmailRequestCount();
		if (user.getEmailRequestRemain() == 0) {
			UserEmailLimit userEmailLimit = UserEmailLimit.of(user.getId());
			userEmailLimitRepository.save(userEmailLimit);
		}

		user.setVerificationCode();
		userRepository.save(user);

		sendVerificationEmail(user, userProfile.getNickname(), request.getEmail(), siteURL, userAgent);
	}

	@Transactional
	public boolean verify(String verificationCode, String address, String agent) {
		Users user = userRepository.findUsersByVerificationCode(verificationCode)
			.orElseThrow(() -> new NotFoundException("verification code not found"));
		UserProfile userProfile = userProfileRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("user not found"));

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

	@Transactional
	public EmailLimitResponseDto getVerificationEmailRemain(Users user) {
		if (user.getEmailRequestRemain() == 0) {
			if (!userEmailLimitRepository.existsById(user.getId())) {
				user.resetEmailRequestCount();
			}
		}
		userRepository.save(user);

		return EmailLimitResponseDto.of(user.getEmailRequestRemain());
	}

	private void sendVerificationEmail(Users user, String nickname, String email, String siteURL, String userAgent) throws
		MessagingException,
		UnsupportedEncodingException {
		String senderName = "Work at_";
		String subject = "이메일 인증을 해주세요";
		String content = "안녕하세요.<br>"
			+ "워케이셔너와 원활한 네트워킹 참가를 위해 이메일 인증을 완료해 주세요.<br>"
			+ "<br>"
			+ "회사인증을 했을 경우 회사인증 배지가 부여됩니다.<br>"
			+ "워크앳을 통해 새로운 업무 문화를 경험해 보세요!<br>"
			+ "<h3 style=\"text-align: center;\"><a href=\"[[URL]]\" target=\"_self\">이메일 인증하기</a></h3>"
			+ "<br>"
			+ "workat.Co";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(email);
		helper.setSubject(subject);

		content = content.replace("[[name]]", nickname);
		String verifyURL =
			siteURL + "/api/v1/user/email-verified?code=" + user.getVerificationCode() + "&address=" + email + "&agent=" + userAgent;

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);
	}

	@Transactional
	public void postUserBlock(Long reportingUserId, Long blockedUserId) {
		Users findReportingUser = userRepository.findById(reportingUserId).orElseThrow(() -> {
			throw new UserNotFoundException(reportingUserId);
		});

		Users findBlockedUser = userRepository.findById(blockedUserId).orElseThrow(() -> {
			throw new UserNotFoundException(blockedUserId);
		});

		UserBlocking userBlocking = UserBlocking.of();
		userBlocking.assignUsers(findReportingUser, findBlockedUser);

		userBlockingRepository.save(userBlocking);
	}

	@Transactional
	public Users saveTestUser(long testOauthId) {
		Users user = Users.of(OauthType.TEST, testOauthId);

		UserProfile userProfile = UserProfile.builder()
			.user(user)
			.nickname("테스트맨_" + testOauthId)
			.imageUrl("") // TODO: 테스트용 계정 프로필 이미지 추가
			.position(DepartmentType.ENGINEER)
			.workingYear(DurationType.JUNIOR)
			.build();

		userProfileRepository.save(userProfile);

		userRepository.save(user);

		return user;
	}
}
