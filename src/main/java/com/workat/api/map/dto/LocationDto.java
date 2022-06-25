package com.workat.api.map.dto;

import com.workat.domain.map.entity.LocationCategory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDto {

	private Long id;

	private LocationCategory category;

	private String placeId;

	private String placeName;

	private String placeUrl;

	private String phone;

	private float x;

	private float y;

	@Builder
	public LocationDto(Long id, LocationCategory category, String placeId, String placeName, String placeUrl,
		String phone, float x, float y) {
		this.id = id;
		this.category = category;
		this.placeId = placeId;
		this.placeName = placeName;
		this.placeUrl = placeUrl;
		this.phone = phone;
		this.x = x;
		this.y = y;
	}
}
