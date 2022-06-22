package com.workat.api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.dto.KakaoOAuthAccessTokenDto;
import com.workat.api.auth.dto.KakaoOAuthTokenRequest;
import com.workat.api.auth.service.KakaoOAuthService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
	private final KakaoOAuthService authorizationService;

	@PostMapping("/v1/auth/token")
	public ResponseEntity<KakaoOAuthAccessTokenDto> issueToken(@RequestBody KakaoOAuthTokenRequest request) {
		KakaoOAuthAccessTokenDto data = authorizationService.auth(request.getCode());

		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
}
