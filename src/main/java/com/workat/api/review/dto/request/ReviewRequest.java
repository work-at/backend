package com.workat.api.review.dto.request;

import java.util.HashSet;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewRequest {

	@ApiModelProperty(name = "reviewTypeNames", notes = "추가할 리뷰 타입 이름", example = "[PARKING, VIEW]")
	private HashSet<String> reviewTypeNames;

	private ReviewRequest(HashSet<String> reviewTypeNames) {
		this.reviewTypeNames = reviewTypeNames;
	}

	public static ReviewRequest of(HashSet<String> reviewTypeNames) {
		return new ReviewRequest(reviewTypeNames);
	}
}
