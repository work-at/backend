package com.workat.api.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.user.dto.SignUpRequest;
import com.workat.api.user.repository.UserRepository;
import com.workat.common.exception.ConflictException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public void signUp(long oauthId, SignUpRequest signUpRequest) {

		userRepository.findByOauthId(oauthId).ifPresent(user -> {
			log.error(String.valueOf(user.getId()));
			throw new ConflictException("user already exists");
		});

		User user = User.builder()
			.id(UUID.randomUUID())
			.nickname(signUpRequest.getNickname())
			.oauthType(OauthType.KAKAO)
			.oauthId(oauthId)
			.position(signUpRequest.getPosition())
			.workingYear(signUpRequest.getWorkingYear())
			.build();

		userRepository.save(user);
	}

}
