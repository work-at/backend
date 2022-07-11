package com.workat.api.review.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeReviewRequest {

	private List<String> reviewTypeNames;

	private CafeReviewRequest(List<String> reviewTypeNames) {
		this.reviewTypeNames = reviewTypeNames;
	}

	public static CafeReviewRequest of(List<String> reviewTypeNames) {
		return new CafeReviewRequest(reviewTypeNames);
	}
}
