package com.workat.api.user.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.auth.service.AuthorizationService;
import com.workat.api.user.dto.SignUpRequest;
import com.workat.api.user.dto.SignUpResponse;
import com.workat.api.user.service.UserService;
import com.workat.domain.user.entity.User;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	private final AuthorizationService authorizationService;

	@ApiOperation(value = "회원가입")
	@PostMapping("api/v1/signup")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {

		final User user = userService.createUser(signUpRequest);
		final long id = userService.signUp(user);

		String accessToken = authorizationService.createAccessToken(id);

		return ResponseEntity.ok()
							 .body(SignUpResponse.of(accessToken));
	}
}
