package com.workat.api.map.dto;

import com.workat.domain.map.entity.LocationCategory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDetailDto extends LocationDto {

	private long id;

	private LocationCategory category;

	private String placeId;

	private String placeName;

	private String placeUrl;

	private String phone;

	private double longitude;

	private double latitude;

	@Builder
	public LocationDetailDto(long id, LocationCategory category, String placeId, String placeName, String placeUrl,
		String phone, double longitude, double latitude) {
		this.id = id;
		this.category = category;
		this.placeId = placeId;
		this.placeName = placeName;
		this.placeUrl = placeUrl;
		this.phone = phone;
		this.longitude = longitude;
		this.latitude = latitude;
	}
}
