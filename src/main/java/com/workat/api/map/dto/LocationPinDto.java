package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationPinDto implements LocationDto {

	private long id;

	private String placeId;

	private double longitude;

	private double latitude;

	public static LocationPinDto of(long id, String placeId, double longitude, double latitude) {
		return new LocationPinDto(id, placeId, longitude, latitude);
	}
}
