package com.workat.domain.user.repository.blocking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.user.entity.UserBlocking;

public interface UserBlockingRepository extends JpaRepository<UserBlocking, Long>, UserBlockingCustomRepository {

	List<UserBlocking> findByReportingUserIdAndBlockedUserId(Long reportUserId, Long blockedUserId);
}
