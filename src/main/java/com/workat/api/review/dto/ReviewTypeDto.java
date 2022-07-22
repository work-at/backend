package com.workat.api.review.dto;

import com.workat.domain.review.BaseReviewType;

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

	@ApiModelProperty(name = "iconType", notes = "리뷰 타입 content", example = "CommonReview1")
	private String iconType;

	private ReviewTypeDto(String name, String content, String iconType) {
		this.name = name;
		this.content = content;
		this.iconType = iconType;
	}

	public static ReviewTypeDto of(BaseReviewType baseReviewData) {
		return new ReviewTypeDto(baseReviewData.getName(), baseReviewData.getContent(), baseReviewData.getIconType());
	}
}
