package com.workat.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findById(UUID id);

	Optional<User> findByOauthTypeAndOauthId(OauthType oauthType, long oAuthId);
}
