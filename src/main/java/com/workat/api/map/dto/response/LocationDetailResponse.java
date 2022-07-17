package com.workat.api.map.dto.response;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.review.dto.ReviewsDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDetailResponse {

	private LocationDetailDto locationDetailDto;

	private ReviewsDto reviewsDto;

	public static LocationDetailResponse of(LocationDetailDto locationDetailDto, ReviewsDto reviewsDto) {
		final LocationDetailResponse locationDetailResponse = new LocationDetailResponse();

		locationDetailResponse.locationDetailDto = locationDetailDto;
		locationDetailResponse.reviewsDto = reviewsDto;

		return locationDetailResponse;
	}
}
