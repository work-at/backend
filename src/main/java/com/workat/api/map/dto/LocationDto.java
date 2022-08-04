package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class LocationDto {

	private long id;

	private String placeId;

	private double longitude;

	private double latitude;

	protected LocationDto(long id, String placeId, double longitude, double latitude) {
		this.id = id;
		this.placeId = placeId;
		this.longitude = longitude;
		this.latitude = latitude;
	}
}
