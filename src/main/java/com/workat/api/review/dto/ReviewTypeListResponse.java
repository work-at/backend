package com.workat.api.review.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewTypeListResponse {

	private List<ReviewTypeResponse> response;

	private ReviewTypeListResponse(List<ReviewTypeResponse> response) {
		this.response = response;
	}

	public static ReviewTypeListResponse of(List<ReviewTypeResponse> response) {
		return new ReviewTypeListResponse(response);
	}
}
