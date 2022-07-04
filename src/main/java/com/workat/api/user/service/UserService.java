package com.workat.api.user.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.user.dto.SignUpRequest;
import com.workat.common.exception.ConflictException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.User;
import com.workat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UUID signUp(User user) {
		OauthType oauthType = user.getOauthType();
		long oauthId = user.getOauthId();

		if (validateUserExistWithOauthId(oauthType, oauthId)
			.isPresent()) {
			log.error("user already exists");

			throw new ConflictException("user already exists");
		}

		userRepository.save(user);

		return user.getId();
	}

	public User createUser(SignUpRequest signUpRequest) {
		final UUID id = UUID.randomUUID();

		return User.builder()
				   .id(id)
				   .oauthType(signUpRequest.getOauthType())
				   .oauthId(signUpRequest.getOauthId())
				   .nickname(signUpRequest.getNickname())
				   .position(signUpRequest.getPosition())
				   .workingYear(signUpRequest.getWorkingYear())
				   .build();

	}

	@Transactional(readOnly = true)
	public Optional<User> validateUserExistWithId(UUID id) {
		// id 로 가입된 유저 있는지 확인
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<User> validateUserExistWithOauthId(OauthType oauthType, long oauthId) {
		// ouath id 로 가입된 유저 있는지 확인
		return userRepository.findByOauthTypeAndOauthId(oauthType, oauthId);
	}

}
