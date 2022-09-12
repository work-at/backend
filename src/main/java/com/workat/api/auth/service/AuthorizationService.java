package com.workat.api.auth.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.workat.api.jwt.service.JwtService;
import com.workat.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorizationService {

	// private final long ACCESS_TOKEN_VALIDATION_TIME = 60 * 60 * 6; // kakao access token 만료 시간과 동일
	private final long ACCESS_TOKEN_VALIDATION_TIME = 60 * 60 * 100000L; // 개발용으로 길게

	private final String TEST_CODE = "tour1";

	private final long TEST_AUTH_ID = 1234; // TODO: 추후 수정 필요

	private final JwtService jwtService;

	public String createAccessToken(long id) {
		HashMap hashMap = new HashMap();
		hashMap.put("id", id);

		return jwtService.createToken(
			jwtService.createClaims(hashMap),
			ACCESS_TOKEN_VALIDATION_TIME);
	}

	public long authForTest(String code) {
		if (!code.equals(TEST_CODE)) {
			throw new BadRequestException("테스트 코드를 확인해주세요.");
		}

		return TEST_AUTH_ID;
	}
}
