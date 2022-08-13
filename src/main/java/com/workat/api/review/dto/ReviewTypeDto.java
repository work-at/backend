package com.workat.api.review.dto;

import com.workat.domain.tag.BaseTag;

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

	private ReviewTypeDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static ReviewTypeDto of(BaseTag baseReviewData) {
		return new ReviewTypeDto(baseReviewData.getName(), baseReviewData.getContent());
	}
}
