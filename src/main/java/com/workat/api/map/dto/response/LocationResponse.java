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

	private List<T> data;

	public static <T extends LocationDto> LocationResponse of(List<T> list) {
		return new LocationResponse<T>(list);
	}
}
