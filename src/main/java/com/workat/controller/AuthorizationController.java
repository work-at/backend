package com.workat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.dto.KakaoOAuthAccessToken;
import com.workat.dto.KakaoOAuthTokenRequest;
import com.workat.service.KakaoOAuthService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
	private final KakaoOAuthService authorizationService;

	@PostMapping("/token")
	public ResponseEntity<KakaoOAuthAccessToken> issueToken(@RequestBody KakaoOAuthTokenRequest request) {
		KakaoOAuthAccessToken data = authorizationService.auth(request.getCode());

		return ResponseEntity.status(HttpStatus.OK).body(data);
	}
}
