package com.workat.api.map.dto.response;

import com.workat.api.map.dto.LocationDto;
import com.workat.api.review.dto.CafeReviewsDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeDetailResponse {

	private LocationDto locationDto;

	private CafeReviewsDto cafeReviewDtos;

	public static CafeDetailResponse of(LocationDto locationDto, CafeReviewsDto cafeReviewDtos) {
		final CafeDetailResponse cafeDetailResponse = new CafeDetailResponse();

		cafeDetailResponse.locationDto = locationDto;
		cafeDetailResponse.cafeReviewDtos = cafeReviewDtos;

		return cafeDetailResponse;
	}
}
