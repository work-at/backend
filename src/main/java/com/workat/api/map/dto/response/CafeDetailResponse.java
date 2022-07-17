package com.workat.api.map.dto.response;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.review.dto.CafeReviewsDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeDetailResponse {

	private LocationDetailDto locationDetailDto;

	private CafeReviewsDto cafeReviewDtos;

	public static CafeDetailResponse of(LocationDetailDto locationDetailDto, CafeReviewsDto cafeReviewDtos) {
		final CafeDetailResponse cafeDetailResponse = new CafeDetailResponse();

		cafeDetailResponse.locationDetailDto = locationDetailDto;
		cafeDetailResponse.cafeReviewDtos = cafeReviewDtos;

		return cafeDetailResponse;
	}
}
