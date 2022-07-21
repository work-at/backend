package com.workat.api.mypage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.workat.api.mypage.dto.request.UserUpdateRequest;
import com.workat.api.mypage.dto.response.MyProfileResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.FileUploadException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.FileUploadUtils;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

	@Value("${resources.upload-folder}")
	private String resourcesLocation;
	@Value("${resources.upload-uri}")
	private String uploadUri;

	private final UserProfileRepository userProfileRepository;

	@Transactional(readOnly = true)
	public MyProfileResponse getMyPage(Long userId) {
		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		return MyProfileResponse.of(userProfile);
	}

	@Transactional
	public void updateMyPage(Long userId, UserUpdateRequest request) {
		userProfileRepository.findFirstByNicknameAndIdNot(request.getNickname(), userId).ifPresent(s -> {
			throw new ConflictException("동일한 닉네임의 유저가 존재합니다.");
		});

		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		userProfile.updateProfile(request.getNickname(), request.getPosition(), request.getWorkingYear(), request.getStory());
		userProfileRepository.save(userProfile);
	}

	@Transactional
	public String uploadProfileImage(Long userId, MultipartFile multipartFile) {
		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));

		if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().isBlank()) {
			throw new BadRequestException("빈 파일입니다.");
		}
		String savedFileName = null;
		try {
			savedFileName = FileUploadUtils.fileUpload(resourcesLocation, String.valueOf(userId), multipartFile.getBytes());
			log.info("save actual path: " + savedFileName);

			savedFileName = savedFileName.replace(resourcesLocation, uploadUri);
			log.info("access image path: " + savedFileName);

			userProfile.updateImage(savedFileName);
			userProfileRepository.save(userProfile);
		} catch (Exception e) {
			throw new FileUploadException(e.getMessage());
		}
		return savedFileName;
	}
}
