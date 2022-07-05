package com.workat.api.map.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationRequest {

	private double x;
	private double y;
	private int radius;
	private int page;

	public static LocationRequest of(double x, double y, int radius, int page) {
		LocationRequest request = new LocationRequest();
		request.x = x;
		request.y = y;
		request.radius = radius;
		request.page = page;
		return request;
	}
}
