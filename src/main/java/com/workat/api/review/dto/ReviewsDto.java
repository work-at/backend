package com.workat.api.review.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewsDto {

	private List<ReviewDto> reviews;

	private boolean userReviewed;

	private ReviewsDto(List<ReviewDto> reviews, boolean userReviewed) {
		this.reviews = reviews;
		this.userReviewed = userReviewed;
	}

	public static ReviewsDto of(List<ReviewDto> reviews, boolean userReviewed) {
		return new ReviewsDto(reviews, userReviewed);
	}
}
