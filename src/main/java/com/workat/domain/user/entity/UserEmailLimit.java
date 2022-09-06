package com.workat.domain.user.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "monthly", timeToLive = 1440)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmailLimit {

	@Id
	private String userId;

	private UserEmailLimit(Long userId) {
		this.userId = String.valueOf(userId);
	}

	public static UserEmailLimit of(Long userId) {
		return new UserEmailLimit(userId);
	}

}
