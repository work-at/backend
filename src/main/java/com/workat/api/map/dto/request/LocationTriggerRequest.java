package com.workat.api.map.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationTriggerRequest {

	private double x;

	private double y;

	private int radius;

	public static LocationTriggerRequest of(double x, double y, int radius) {
		LocationTriggerRequest request = new LocationTriggerRequest();
		request.x = x;
		request.y = y;
		request.radius = radius;
		return request;
	}
}
