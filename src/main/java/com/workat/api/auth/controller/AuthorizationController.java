package com.workat.api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.dto.request.KakaoOauthTokenRequest;
import com.workat.api.auth.dto.request.TestTokenRequest;
import com.workat.api.auth.dto.response.AuthResponse;
import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.auth.service.KakaoOauthService;
import com.workat.api.user.service.UserService;
import com.workat.domain.auth.OauthType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Authorization Api")
@RequiredArgsConstructor
@RestController
public class AuthorizationController {

	private final KakaoOauthService kakaoOauthService;

	private final AuthorizationService authorizationService;

	private final UserService userService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "login with kakao", notes = "카카오 로그인")
	@PostMapping("/api/v1/auth/token/kakao")
	public ResponseEntity<AuthResponse> login(@RequestBody KakaoOauthTokenRequest request) {
		// 카카오 인증
		final long kakaoId = kakaoOauthService.auth(request.getCode());

		// 로그인 처리
		final AuthResponse authResponse = userService.loginWithOauth(OauthType.KAKAO, kakaoId);

		return ResponseEntity.ok(authResponse);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "login for test", notes = "테스트 로그인")
	@PostMapping("/api/v1/auth/token/test")
	public ResponseEntity<AuthResponse> loginForTest(@RequestBody TestTokenRequest request) {
		long testId = authorizationService.authForTest(request.getCode());

		final AuthResponse authResponse = userService.loginForTest(testId);

		return ResponseEntity.ok(authResponse);
	}
}
