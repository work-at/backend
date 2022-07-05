package com.workat.api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.dto.AuthResponse;
import com.workat.api.auth.dto.KakaoOauthTokenRequest;
import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.auth.service.KakaoOauthService;
import com.workat.api.user.service.UserService;
import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Authorization")
@RequiredArgsConstructor
@RestController
public class AuthorizationController {

	private final KakaoOauthService kakaoOauthService;

	private final AuthorizationService authorizationService;

	private final UserService userService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "login", notes = "카카오 로그인")
	@PostMapping("/api/v1/auth/token/kakao")
	public ResponseEntity<AuthResponse> login(@RequestBody KakaoOauthTokenRequest request) {

		// 카카오 인증
		final long kakaoId = kakaoOauthService.auth(request.getCode());

		// DB 유저 존재 확인
		final Users user = userService.validateUserExistWithOauthId(OauthType.KAKAO, kakaoId).orElse(null);

		if (user == null) {
			return ResponseEntity.ok()
								 .body(AuthResponse.ResponseForSignup(kakaoId));
		}

		final String accessToken = authorizationService.createAccessToken(user.getId());

		return ResponseEntity.ok()
							 .body(AuthResponse.ResponseForLogin(accessToken));
	}
}
