package com.workat.api.review.dto;

import com.workat.domain.review.CafeReviewType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeReviewDto {

	private ReviewTypeDto reviewTypeDto;

	private long count;

	private CafeReviewDto(ReviewTypeDto reviewTypeDto, long count) {
		this.reviewTypeDto = reviewTypeDto;
		this.count = count;
	}

	public static CafeReviewDto of(CafeReviewType cafeReviewType, long count) {
		return new CafeReviewDto(ReviewTypeDto.of(cafeReviewType), count);
	}
}
