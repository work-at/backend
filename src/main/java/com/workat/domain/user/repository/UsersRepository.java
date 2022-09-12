package com.workat.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByOauthTypeAndOauthId(OauthType oauthType, Long oauthId);

	Optional<Users> findAllByOauthType(OauthType oauthType);

	Optional<Users> findUsersByVerificationCode(String verificationCode);
}
