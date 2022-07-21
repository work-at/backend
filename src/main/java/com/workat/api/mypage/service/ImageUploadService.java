package com.workat.api.mypage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.workat.common.exception.BadRequestException;
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
public class ImageUploadService {

	@Value("${resources.upload-folder}")
	private String resourcesLocation;
	@Value("${resources.upload-uri}")
	private String uploadUri;

	private final UserProfileRepository userProfileRepository;

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
