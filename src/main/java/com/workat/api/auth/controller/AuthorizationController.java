package com.workat.api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.dto.KakaoOauthAccessTokenDto;
import com.workat.api.auth.dto.KakaoOauthTokenRequest;
import com.workat.api.auth.service.KakaoOauthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthorizationController {

	private final KakaoOauthService kakaoOauthService;

	@PostMapping("/api/v1/auth/token")
	public ResponseEntity<KakaoOauthAccessTokenDto> issueToken(@RequestBody KakaoOauthTokenRequest request) {
		KakaoOauthAccessTokenDto data = kakaoOauthService.getAccessToken(request.getCode());

		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
}
