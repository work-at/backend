package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationUpdateRequest {

	private double x;

	private double y;

	private int radius;

	public static LocationUpdateRequest of(double x, double y, int radius) {
		LocationUpdateRequest request = new LocationUpdateRequest();
		request.x = x;
		request.y = y;
		request.radius = radius;
		return request;
	}
}
