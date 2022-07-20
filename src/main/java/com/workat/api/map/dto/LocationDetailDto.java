package com.workat.api.map.dto;

import com.workat.domain.map.entity.LocationCategory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDetailDto implements LocationDto {

	private long id;

	private LocationCategory category;

	private String placeId;

	private String placeName;

	private String placeUrl;

	private String phone;

	private String fullImageUrl;

	private String thumbnailImageUrl;

	private double longitude;

	private double latitude;
}
