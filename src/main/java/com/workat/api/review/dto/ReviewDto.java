package com.workat.api.review.dto;

import com.workat.domain.tag.BaseTag;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewDto {

	@ApiModelProperty(name = "reviewType", notes = "리뷰 타입")
	private ReviewTypeDto reviewType;

	@ApiModelProperty(name = "count", notes = "리뷰 count", example = "1")
	private long count;

	private ReviewDto(ReviewTypeDto reviewTypeDto, long count) {
		this.reviewType = reviewTypeDto;
		this.count = count;
	}

	public static ReviewDto of(BaseTag reviewType, long count) {
		return new ReviewDto(ReviewTypeDto.of(reviewType), count);
	}
}
