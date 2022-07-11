package com.workat.api.review.dto;

import com.workat.domain.review.CafeReviewType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeReviewTypeDto {

	private CafeReviewType cafeReviewType;

	private CafeReviewTypeDto(CafeReviewType cafeReviewType) {
		this.cafeReviewType = cafeReviewType;
	}

	public static CafeReviewTypeDto of(CafeReviewType cafeReviewType) {
		return new CafeReviewTypeDto(cafeReviewType);
	}
}
