package com.workat.api.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.auth.dto.response.AuthResponse;
import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.user.dto.SignUpResponse;
import com.workat.api.user.dto.request.SignUpRequest;
import com.workat.common.exception.ConflictException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UsersRepository userRepository;

	private final AuthorizationService authorizationService;

	public AuthResponse login(OauthType oauthType, long oauthId) {
		final boolean userExist = validateUserExistWithOauthId(oauthType, oauthId);

		if (!userExist) {
			return AuthResponse.ResponseForSignup(oauthId);
		}

		final String accessToken = authorizationService.createAccessToken(oauthId);

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

		final Users user = convertUser(signUpRequest);
		userRepository.save(user);

		final long id = user.getId();
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
		return userRepository.findFirstByNickname(nickname).isPresent();
	}

	@Transactional(readOnly = true)
	public boolean validateUserExistWithOauthId(OauthType oauthType, long oauthId) {
		// ouath id 로 가입된 유저 있는지 확인
		final Optional<Users> usersOptional = userRepository.findByOauthTypeAndOauthId(oauthType, oauthId);

		return usersOptional.isPresent();
	}

	private Users convertUser(SignUpRequest signUpRequest) {
		return Users.builder()
			.oauthType(signUpRequest.getOauthType())
			.oauthId(signUpRequest.getOauthId())
			.nickname(signUpRequest.getNickname())
			.position(signUpRequest.getPosition())
			.workingYear(signUpRequest.getWorkingYear())
			.build();
	}

}
