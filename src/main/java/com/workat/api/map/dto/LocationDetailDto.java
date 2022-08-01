package com.workat.api.map.dto;

import com.workat.domain.map.entity.LocationCategory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDetailDto extends LocationDto {

	private LocationCategory category;

	private String placeName;

	private String roadAddressName;

	private String fullImageUrl;

	private String phone;

	private String placeUrl;

	@Builder
	public LocationDetailDto(long id, String placeId, double latitude, double longitude,
		LocationCategory category, String placeName, String roadAddressName, String fullImageUrl, String phone,
		String placeUrl) {
		super(id, placeId, latitude, longitude);

		this.category = category;
		this.placeName = placeName;
		this.roadAddressName = roadAddressName;
		this.fullImageUrl = fullImageUrl;
		this.phone = phone;
		this.placeUrl = placeUrl;
	}
	
}
