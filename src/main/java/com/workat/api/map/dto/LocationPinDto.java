package com.workat.api.map.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationPinDto extends LocationDto {

	public LocationPinDto(long id, String placeId, double latitude, double longitude) {
		super(id, placeId, latitude, longitude);
	}

	static public LocationPinDto of(long id, String placeId, double latitude, double longitude) {
		return new LocationPinDto(id, placeId, latitude, longitude);
	}
}
