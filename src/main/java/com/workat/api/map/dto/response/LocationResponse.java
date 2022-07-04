package com.workat.api.map.dto.response;

import java.util.List;

import com.workat.api.map.dto.MapLocationDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationResponse {

	private List<MapLocationDto> locations;

	public static LocationResponse of(List<MapLocationDto> locations) {
		LocationResponse response = new LocationResponse();
		response.locations = locations;
		return response;
	}
}
