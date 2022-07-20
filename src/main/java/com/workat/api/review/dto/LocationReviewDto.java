package com.workat.api.review.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationReviewDto {

	@ApiModelProperty(name = "reviews", notes = "리뷰들")
	private List<ReviewDto> reviews;

	@ApiModelProperty(name = "userReviewed", notes = "유저가 리뷰 남겼는지 여부")
	private boolean userReviewed;

	private LocationReviewDto(List<ReviewDto> reviewDtos, boolean userReviewed) {
		this.reviews = reviewDtos;
		this.userReviewed = userReviewed;
	}

	public static LocationReviewDto of(List<ReviewDto> reviewDtos, boolean userReviewed) {
		return new LocationReviewDto(reviewDtos, userReviewed);
	}
}