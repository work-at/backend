package com.workat.api.map.dto.response;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.review.dto.ReviewsDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeDetailResponse {

	private LocationDetailDto locationDetailDto;

	private ReviewsDto reviewsDto;

	public static CafeDetailResponse of(LocationDetailDto locationDetailDto, ReviewsDto reviewsDto) {
		final CafeDetailResponse cafeDetailResponse = new CafeDetailResponse();

		cafeDetailResponse.locationDetailDto = locationDetailDto;
		cafeDetailResponse.reviewsDto = reviewsDto;

		return cafeDetailResponse;
	}
}
