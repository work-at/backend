package com.workat.domain.user.repository.blocking;

import static com.workat.domain.user.entity.QUserBlocking.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.user.entity.UserBlocking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserBlockingCustomRepositoryImpl implements UserBlockingCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<UserBlocking> findAllByUserId(Long userId) {
		return jpaQueryFactory.selectFrom(userBlocking)
			.where(userBlocking.reportingUser.id.eq(userId).or(userBlocking.blockedUser.id.eq(userId)))
			.fetch();
	}

	@Override
	public List<UserBlocking> findAllByReportingUserId(Long userId) {
		return jpaQueryFactory.selectFrom(userBlocking)
			.where(userBlocking.reportingUser.id.eq(userId))
			.fetch();
	}

	@Override
	public List<UserBlocking> findAllByBlockedUserId(Long userId) {
		return jpaQueryFactory.selectFrom(userBlocking)
			.where(userBlocking.blockedUser.id.eq(userId))
			.fetch();
	}
}
