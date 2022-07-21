package com.workat.api.mypage.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workat.api.mypage.service.ImageUploadService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "Image upload Api")
@RequiredArgsConstructor
@RestController
public class ImageUploadController {

	private final ImageUploadService imageUploadService;

	@PostMapping("/upload/images")
	public String uploadProfileImage(@UserValidation Users user, @RequestParam("file") MultipartFile multipartFile) {
		String redirectURI = imageUploadService.uploadProfileImage(user.getId(), multipartFile);
		return "redirect:" + redirectURI;
	}
}
