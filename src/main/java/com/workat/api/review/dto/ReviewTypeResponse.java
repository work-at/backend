package com.workat.api.review.dto;

import com.workat.domain.review.BaseReviewType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewTypeResponse {

	private String name;
	private String content;

	private ReviewTypeResponse(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static ReviewTypeResponse of(BaseReviewType baseReviewData) {
		return new ReviewTypeResponse(baseReviewData.getName(), baseReviewData.getContent());
	}
}
