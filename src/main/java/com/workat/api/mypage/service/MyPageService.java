package com.workat.api.mypage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.mypage.dto.MyProfileResponse;
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
}
