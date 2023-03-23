package com.workat.api.user.service.data;

import com.workat.common.exception.NotFoundException;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDataService {

	private final UsersRepository userRepository;

	private final UserProfileRepository userProfileRepository;

	public Users save(Users user) {
		return userRepository.save(user);
	}

	public UserProfile saveProfile(UserProfile userProfile) {
		return userProfileRepository.save(userProfile);
	}

	public Users getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> {
			String format = String.format("user not found (id: %d)", userId);
			throw new NotFoundException(format);
		});
	}
}
