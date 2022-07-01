package com.workat.api.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workat.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByOauthId(long oAuthId);
}
