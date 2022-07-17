package com.workat.api.review.dto;

import com.workat.domain.review.BaseReviewType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewDto {

	private ReviewTypeDto reviewTypeDto;

	private long count;

	private ReviewDto(ReviewTypeDto reviewTypeDto, long count) {
		this.reviewTypeDto = reviewTypeDto;
		this.count = count;
	}

	public static ReviewDto of(BaseReviewType reviewType, long count) {
		return new ReviewDto(ReviewTypeDto.of(reviewType), count);
	}
}
