package com.workat.api.mypage.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.mypage.dto.request.UserUpdateRequest;
import com.workat.api.mypage.dto.response.MyProfileResponse;
import com.workat.api.mypage.service.MyPageService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "My Page Api")
@RequiredArgsConstructor
@RestController
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/api/v1/my-page")
	public ResponseEntity<MyProfileResponse> getMyProfile(@UserValidation Users user) {
		MyProfileResponse response = myPageService.getMyPage(user.getId());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/api/v1/my-page")
	public ResponseEntity<?> updateMyProfile(@UserValidation Users user, @Valid @RequestBody UserUpdateRequest request) {
		myPageService.updateMyPage(user.getId(), request);
		return ResponseEntity.ok().build();
	}
}
