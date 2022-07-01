package com.workat.api.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.dto.KakaoOauthIdDto;
import com.workat.api.auth.service.KakaoOauthService;
import com.workat.api.user.dto.SignUpDto;
import com.workat.api.user.service.UserService;
import com.workat.common.annotation.OauthTokenValidation;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final KakaoOauthService kakaoOauthService;

	@ApiOperation(value = "회원가입")
	@PostMapping("api/v1/signup")
	@OauthTokenValidation
	public ResponseEntity signUp(@RequestHeader("Authorization") String accessToken, @RequestBody SignUpDto signUpDto) {
		KakaoOauthIdDto kakaoOauthIdDto = kakaoOauthService.getId(accessToken);
		userService.signUp(kakaoOauthIdDto, signUpDto);

		return ResponseEntity.ok().build();
	}
}
