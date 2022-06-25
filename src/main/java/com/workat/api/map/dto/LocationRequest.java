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

	@Builder
	public LocationRequest(float x, float y, int radius, int page) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.page = page;
	}

}
