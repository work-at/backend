package com.workat.api.review.dto.response;

import java.util.List;

import com.workat.api.review.dto.ReviewTypeDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewTypeListResponse {

	@ApiModelProperty(name = "response", notes = "리뷰 타입")
	private List<ReviewTypeDto> response;

	private ReviewTypeListResponse(List<ReviewTypeDto> response) {
		this.response = response;
	}

	public static ReviewTypeListResponse of(List<ReviewTypeDto> response) {
		return new ReviewTypeListResponse(response);
	}
}
