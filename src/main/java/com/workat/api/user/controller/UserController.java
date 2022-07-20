package com.workat.api.user.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.user.dto.SignUpResponse;
import com.workat.api.user.dto.request.SignUpRequest;
import com.workat.api.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "User Api")
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	private final AuthorizationService authorizationService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "sign up", notes = "회원 가입")
	@PostMapping("/api/v1/user/signup")
	public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		SignUpResponse signUpResponse = userService.signUp(signUpRequest);
		return ResponseEntity.ok(signUpResponse);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "isUserNicknameExists", notes = "유저 닉네임 확인, true=이미존재/false=닉네임가능")
	@GetMapping("/api/v1/user/validation")
	public ResponseEntity<Boolean> checkNickname(@RequestParam("nickname") String nickname) {
		boolean response = userService.isUserNicknameExists(nickname);
		return ResponseEntity.ok(response);
	}
}
