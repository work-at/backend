package com.workat.api.review.dto.request;

import java.util.HashSet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewRequest {

	private HashSet<String> reviewTypeNames;

	private ReviewRequest(HashSet<String> reviewTypeNames) {
		this.reviewTypeNames = reviewTypeNames;
	}

	public static ReviewRequest of(HashSet<String> reviewTypeNames) {
		return new ReviewRequest(reviewTypeNames);
	}
}
