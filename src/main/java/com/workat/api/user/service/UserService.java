package com.workat.api.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.auth.dto.KakaoOauthIdDto;
import com.workat.api.user.dto.SignUpDto;
import com.workat.api.user.repository.UserRepository;
import com.workat.common.exception.ConflictException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public void signUp(KakaoOauthIdDto idDto, SignUpDto signUpDto) {
		final long oauthId = idDto.getId();

		userRepository.findByOauthId(oauthId).ifPresent(user -> {
			log.error(String.valueOf(user.getId()));
			throw new ConflictException("user already exists");
		});

		User user = User.builder()
			.id(UUID.randomUUID())
			.nickname(signUpDto.getNickname())
			.oauthType(OauthType.KAKAO)
			.oauthId(oauthId)
			.position(signUpDto.getPosition())
			.workingYear(signUpDto.getWorkingYear())
			.build();

		userRepository.save(user);
	}

}
