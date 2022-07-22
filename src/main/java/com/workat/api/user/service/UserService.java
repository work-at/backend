package com.workat.api.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.workat.api.auth.dto.response.AuthResponse;
import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.user.dto.SignUpResponse;
import com.workat.api.user.dto.request.SignUpRequest;
import com.workat.api.user.dto.request.UserUpdateRequest;
import com.workat.api.user.dto.response.MyProfileResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.FileUploadException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.FileUploadUtils;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	@Value("${resources.upload-folder}")
	private String resourcesLocation;
	@Value("${resources.upload-uri}")
	private String uploadUri;

	private final UsersRepository userRepository;

	private final UserProfileRepository userProfileRepository;

	private final AuthorizationService authorizationService;

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
		return MyProfileResponse.of(userProfile);
	}

	@Transactional
	public void updateUserProfile(Long userId, UserUpdateRequest request) {
		userProfileRepository.findFirstByNicknameAndIdNot(request.getNickname(), userId).ifPresent(s -> {
			throw new ConflictException("동일한 닉네임의 유저가 존재합니다.");
		});

		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		userProfile.updateProfile(request.getNickname(), request.getPosition(), request.getWorkingYear(), request.getStory());
		userProfileRepository.save(userProfile);
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
}
