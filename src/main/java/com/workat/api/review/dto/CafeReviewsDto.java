package com.workat.api.review.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CafeReviewsDto {

	private List<CafeReviewDto> cafeReviews;

	private boolean userReviewed;

	private CafeReviewsDto(List<CafeReviewDto> cafeReviews, boolean userReviewed) {
		this.cafeReviews = cafeReviews;
		this.userReviewed = userReviewed;
	}

	public static CafeReviewsDto of(List<CafeReviewDto> cafeReviews, boolean userReviewed) {
		return new CafeReviewsDto(cafeReviews, userReviewed);
	}
}
