package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationRequest {

	private float x;

	private float y;

	private int radius;

	private int page;

	public static LocationRequest of(float x, float y, int radius, int page) {
		LocationRequest request = new LocationRequest();
		request.x = x;
		request.y = y;
		request.radius = radius;
		request.page = page;
		return request;
	}
}
