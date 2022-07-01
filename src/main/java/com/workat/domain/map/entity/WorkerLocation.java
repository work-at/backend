package com.workat.domain.map.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash(value = "worker", timeToLive = 648000) // TODO: set time to live
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerLocation {

	@Id
	private String userId;

	private String longitude;

	private String latitude;

	private String address;

	private WorkerLocation(String userId, String longitude, String latitude, String address) {
		this.userId = userId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
	}

	public static WorkerLocation of(String userId, String longitude, String latitude, String address) {
		return new WorkerLocation(userId, longitude, latitude, address);
	}
}
