package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationPinDto extends LocationDto {

	public LocationPinDto(long id, String placeId, double longitude, double latitude) {
		super(id, placeId, longitude, latitude);
	}

	public static LocationPinDto of(long id, String placeId, double longitude, double latitude) {
		return new LocationPinDto(id, placeId, longitude, latitude);
	}
}
