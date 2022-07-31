package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class LocationDto {

	private long id;

	private String placeId;

	private double latitude;

	private double longitude;

	protected LocationDto(long id, String placeId, double latitude, double longitude) {
		this.id = id;
		this.placeId = placeId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
