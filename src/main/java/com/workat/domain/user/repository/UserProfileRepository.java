package com.workat.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.user.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

	Optional<UserProfile> findFirstByNickname(String nickname);
}
