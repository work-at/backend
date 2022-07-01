package com.workat.domain.map.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@RedisHash(value = "worker", timeToLive = 648000) // TODO: set time to live
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerLocation {

	@Id
	private String userId;

	@GeoIndexed
	private Point location;

	private String address;

	private WorkerLocation(String userId, String longitude, String latitude, String address) {
		this.userId = userId;
		this.location = new Point(Double.parseDouble(longitude), Double.parseDouble(latitude));
		this.address = address;
	}

	public static WorkerLocation of(String userId, String longitude, String latitude, String address) {
		return new WorkerLocation(userId, longitude, latitude, address);
	}
}
