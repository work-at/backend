package com.workat.api.review.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewTypeListResponse {

	@ApiModelProperty(name = "response", notes = "리뷰 타입", example = "{\"response\":[{\"name\":\"PARKING\",\"content\":\"주차하기 편해요~\"},{\"name\":\"VIEW\",\"content\":\"뷰가 좋아요\"},{\"name\":\"NOT_CROWDED\",\"content\":\"한적해요~\"},{\"name\":\"NIGHT_VIEW\",\"content\":\"야경이 예뻐요~\"},{\"name\":\"SPACE\",\"content\":\"넓고 깨끗해요~\"},{\"name\":\"WIFI\",\"content\":\"와이파이가 빵빵해요!\"},{\"name\":\"POWER\",\"content\":\"콘센트 자리 많아요!\"},{\"name\":\"SEAT\",\"content\":\"좌석이 편해요\"},{\"name\":\"QUIET\",\"content\":\"조용한 공간이 있어요!\"},{\"name\":\"FOCUS\",\"content\":\"집중이 잘돼요\"}]}")
	private List<ReviewTypeResponse> response;

	private ReviewTypeListResponse(List<ReviewTypeResponse> response) {
		this.response = response;
	}

	public static ReviewTypeListResponse of(List<ReviewTypeResponse> response) {
		return new ReviewTypeListResponse(response);
	}
}
