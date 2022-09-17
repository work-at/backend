package com.workat.api.accommodation.dto;

import java.util.List;

import com.workat.domain.tag.dto.TagCountDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccommodationReviewDto {

	@ApiModelProperty(name = "reviews")
	private List<TagCountDto> reviews;

	@ApiModelProperty(name = "userReviewed", notes = "유저가 리뷰를 남겼는지 여부")
	private boolean userReviewed;

	@ApiModelProperty(name = "userCount", notes = "리뷰 남긴 유저 수")
	private int userCount;

	private AccommodationReviewDto(List<TagCountDto> reviews, boolean userReviewed, int userCount) {
		this.reviews = reviews;
		this.userReviewed = userReviewed;
		this.userCount = userCount;
	}

	public static AccommodationReviewDto of(List<TagCountDto> reviews, boolean userReviewed, int userCount) {
		return new AccommodationReviewDto(reviews, userReviewed, userCount);
	}
}
