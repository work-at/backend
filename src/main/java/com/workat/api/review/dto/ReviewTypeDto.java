package com.workat.api.review.dto;

import com.workat.domain.tag.review.ReviewTag;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewTypeDto {

	@ApiModelProperty(name = "name", notes = "리뷰 타입 name", example = "VIEW")
	private String name;

	@ApiModelProperty(name = "content", notes = "리뷰 타입 content", example = "뷰가 좋아요")
	private String content;

	public ReviewTypeDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static ReviewTypeDto of(ReviewTag baseReviewData) {
		return new ReviewTypeDto(baseReviewData.getName(), baseReviewData.getContent2());
	}

	public static ReviewTypeDto summaryOf(ReviewTag baseReviewData) {
		return new ReviewTypeDto(baseReviewData.getName(), baseReviewData.getSummary());
	}
}
