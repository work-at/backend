package com.workat.api.user.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workat.api.user.dto.SignUpResponse;
import com.workat.api.user.dto.request.EmailCertifyRequest;
import com.workat.api.user.dto.request.SignUpRequest;
import com.workat.api.user.dto.request.UserUpdateRequest;
import com.workat.api.user.dto.response.EmailLimitResponseDto;
import com.workat.api.user.dto.response.MyProfileResponse;
import com.workat.api.user.service.UserService;
import com.workat.common.annotation.UserValidation;
import com.workat.common.util.UrlUtils;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "User Api")
@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

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

	@GetMapping("/api/v1/user/self")
	public ResponseEntity<MyProfileResponse> getSelfUserProfile(@UserValidation Users user, HttpServletRequest request) {
		String baseUrl = UrlUtils.getBaseUrl(request);
		MyProfileResponse response = userService.getSelfUserProfile(user, baseUrl);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/api/v1/user")
	public ResponseEntity<?> updateUserProfile(@UserValidation Users user, @Valid @RequestBody UserUpdateRequest request) {
		userService.updateUserProfile(user, request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/api/v1/user/image")
	public String uploadProfileImage(@UserValidation Users user, @RequestParam("file") MultipartFile multipartFile) {
		String redirectURI = userService.uploadProfileImage(user.getId(), multipartFile);
		return "redirect:" + redirectURI;
	}

	@PostMapping("/api/v1/user/verify")
	public ResponseEntity<?> sendCompanyVerifyEmail(@UserValidation Users user, @Valid @RequestBody EmailCertifyRequest emailCertifyRequest, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		userService.sendCompanyVerifyEmail(user, emailCertifyRequest, getSiteURL(request), getUserAgent(request));
		return ResponseEntity.ok().build();
	}

	private static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@GetMapping("/api/v1/user/verify/remaining-attempts")
	public ResponseEntity<EmailLimitResponseDto> getCompanyVerifyEmailRemain(@UserValidation Users user) {
		EmailLimitResponseDto response = userService.getVerificationEmailRemain(user);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/api/v1/user/tracking")
	public ResponseEntity<?> toggleUserTracking(@UserValidation Users user, @RequestParam boolean turnOff) {
		userService.changeUserTracking(user, turnOff);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/api/v1/user/email-verified")
	public String verifyUser(@RequestParam("code") String code, @RequestParam String address, @RequestParam String agent, HttpServletResponse httpServletResponse) throws IOException {
		// TODO: 메일 인증 후 redirect 페이지 여부에 따라 변경 가능
		if (userService.verify(code, address, agent)) {
			if (agent.indexOf("ANDROID_APP") > -1) {
				httpServletResponse.sendRedirect("https://play.google.com/store/apps/details?id=com.workat_");
			}

			httpServletResponse.sendRedirect("https://workat.o-r.kr/accommodation");
		}
		return "verify_fail";
	}

	@PostMapping("/api/v1/user/blocking")
	public void postUserBlock(@UserValidation Users user, @RequestParam Long blockUserId) {
		userService.postUserBlock(user.getId(), blockUserId);
	}
}
