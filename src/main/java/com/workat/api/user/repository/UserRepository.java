package com.workat.api.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.user.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByOauthId(long oAuthId);
}
