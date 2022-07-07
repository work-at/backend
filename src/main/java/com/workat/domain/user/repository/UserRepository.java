package com.workat.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findById(Long id);

	Optional<Users> findFirstByNickname(String nickname);

	Optional<Users> findByOauthTypeAndOauthId(OauthType oauthType, Long oauthId);
}
