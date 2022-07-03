package com.workat.api.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByOauthId(long oAuthId);
}
