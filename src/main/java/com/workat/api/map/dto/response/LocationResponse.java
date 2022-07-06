package com.workat.api.map.dto.response;

import java.util.List;

import com.workat.api.map.dto.LocationDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationResponse {

	private List<LocationDto> locations;

	public static LocationResponse of(List<LocationDto> locations) {
		LocationResponse response = new LocationResponse();
		response.locations = locations;
		return response;
	}
}
