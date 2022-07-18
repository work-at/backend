package com.workat.api.auth.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.workat.api.jwt.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorizationService {

	// private final long ACCESS_TOKEN_VALIDATION_TIME = 60 * 60 * 6; // kakao access token 만료 시간과 동일
	private final long ACCESS_TOKEN_VALIDATION_TIME = 60 * 60 * 100000L; // 개발용으로 길게

	private final JwtService jwtService;

	public String createAccessToken(long id) {
		HashMap hashMap = new HashMap();
		hashMap.put("id", id);

		return jwtService.createToken(
			jwtService.createClaims(hashMap),
			ACCESS_TOKEN_VALIDATION_TIME);
	}
}
