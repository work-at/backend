package com.workat.api.map.dto;

import com.workat.domain.map.entity.LocationCategory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationBriefDto implements LocationDto {

	private long id;

	private String placeId;

	private double latitude;

	private double longitude;

	private LocationCategory category;

	private String placeName;

	private String roadAddressName;

	private String thumbnailImageUrl;

	private int reviewCount;
}
