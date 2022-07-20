package com.workat.api.mypage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.mypage.dto.request.UserUpdateRequest;
import com.workat.api.mypage.dto.response.MyProfileResponse;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

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
}
