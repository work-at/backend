package com.workat.domain.user.repository.blocking;

import java.util.List;
import java.util.Optional;

import com.workat.domain.user.entity.UserBlocking;

public interface UserBlockingCustomRepository {

	List<UserBlocking> findAllByUserId(Long userId);

	List<UserBlocking> findAllByReportingUserId(Long userId);

	List<UserBlocking> findAllByBlockedUserId(Long userId);
}
