package com.workat.api.review.dto;

import com.workat.domain.review.BaseReviewType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewTypeDto {

	private String name;
	private String content;

	private ReviewTypeDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static ReviewTypeDto of(BaseReviewType baseReviewData) {
		return new ReviewTypeDto(baseReviewData.getName(), baseReviewData.getContent());
	}
}
