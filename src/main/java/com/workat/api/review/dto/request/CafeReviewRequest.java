package com.workat.api.review.dto.request;

import java.util.HashSet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeReviewRequest {

	private HashSet<String> reviewTypeNames;

	private CafeReviewRequest(HashSet<String> reviewTypeNames) {
		this.reviewTypeNames = reviewTypeNames;
	}

	public static CafeReviewRequest of(HashSet<String> reviewTypeNames) {
		return new CafeReviewRequest(reviewTypeNames);
	}
}
