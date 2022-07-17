package com.workat.api.map.dto.response;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.review.dto.LocationReviewDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationDetailResponse {

	@ApiModelProperty(name = "locationDetail", notes = "로케이션 상세 정보")
	private LocationDetailDto locationDetail;

	@ApiModelProperty(name = "locationReview", notes = "로케이션 리뷰")
	private LocationReviewDto locationReview;

	public static LocationDetailResponse of(LocationDetailDto locationDetailDto, LocationReviewDto locationReviewDto) {
		final LocationDetailResponse locationDetailResponse = new LocationDetailResponse();

		locationDetailResponse.locationDetail = locationDetailDto;
		locationDetailResponse.locationReview = locationReviewDto;

		return locationDetailResponse;
	}
}
