package com.workat.api.map.dto;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDetailDto extends LocationDto {

	private LocationType category;

	private String placeName;

	private String roadAddressName;

	private String fullImageUrl;

	private String phone;

	private String placeUrl;

	@Builder
	public LocationDetailDto(long id, String placeId, double longitude, double latitude,
		LocationType category, String placeName, String roadAddressName, String fullImageUrl, String phone,
		String placeUrl) {
		super(id, placeId, longitude, latitude);

		this.category = category;
		this.placeName = placeName;
		this.roadAddressName = roadAddressName;
		this.fullImageUrl = fullImageUrl;
		this.phone = phone;
		this.placeUrl = placeUrl;
	}

	public static LocationDetailDto from(Location location, String fullImageUrl) {
		
		return LocationDetailDto.builder()
			.id(location.getId())
			.placeId(location.getPlaceId())
			.latitude(location.getLatitude())
			.longitude(location.getLongitude())
			.category(location.getType())
			.placeName(location.getPlaceName())
			.roadAddressName(location.getRoadAddressName())
			.fullImageUrl(fullImageUrl)
			.phone(location.getPhone())
			.placeUrl(location.getPlaceUrl())
			.build();
	}

}
