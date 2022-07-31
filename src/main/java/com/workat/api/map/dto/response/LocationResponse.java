package com.workat.api.map.dto.response;

import java.util.List;

import com.workat.api.map.dto.LocationDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationResponse<T extends LocationDto> {

	private List<T> locations;

	public static <T extends LocationDto> LocationResponse<T> of(List<T> list) {
		return new LocationResponse<>(list);
	}
}
