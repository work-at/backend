package com.workat.api.review.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewsDto {

	private List<ReviewDto> reviewDtos;

	private boolean userReviewed;

	private ReviewsDto(List<ReviewDto> reviewDtos, boolean userReviewed) {
		this.reviewDtos = reviewDtos;
		this.userReviewed = userReviewed;
	}

	public static ReviewsDto of(List<ReviewDto> reviewDtos, boolean userReviewed) {
		return new ReviewsDto(reviewDtos, userReviewed);
	}
}
