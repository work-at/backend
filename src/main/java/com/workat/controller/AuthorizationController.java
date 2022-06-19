package com.workat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.dto.KakaoOAuthAccessTokenDto;
import com.workat.dto.KakaoOAuthTokenRequestDto;
import com.workat.service.KakaoOAuthService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
	private final KakaoOAuthService authorizationService;

	@PostMapping("/v1/auth/token")
	public ResponseEntity<KakaoOAuthAccessTokenDto> issueToken(@RequestBody KakaoOAuthTokenRequestDto request) {
		KakaoOAuthAccessTokenDto data = authorizationService.auth(request.getCode());

		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
}
