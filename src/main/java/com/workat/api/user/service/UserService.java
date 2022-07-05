package com.workat.api.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.user.dto.request.SignUpRequest;
import com.workat.common.exception.ConflictException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public long signUp(Users user) {
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

	public Users createUser(SignUpRequest signUpRequest) {
		return Users.builder()
					.oauthType(signUpRequest.getOauthType())
					.oauthId(signUpRequest.getOauthId())
					.nickname(signUpRequest.getNickname())
					.position(signUpRequest.getPosition())
					.workingYear(signUpRequest.getWorkingYear())
					.build();

	}

	@Transactional(readOnly = true)
	public Optional<Users> validateUserExistWithId(long id) {
		// id 로 가입된 유저 있는지 확인
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Users> validateUserExistWithOauthId(OauthType oauthType, long oauthId) {
		// ouath id 로 가입된 유저 있는지 확인
		return userRepository.findByOauthTypeAndOauthId(oauthType, oauthId);
	}

}
