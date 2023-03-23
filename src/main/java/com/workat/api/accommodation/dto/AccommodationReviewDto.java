package com.workat.api.accommodation.dto;

import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReview;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagSummaryDto;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;
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

	public static AccommodationReviewDto from(Accommodation accommodation, boolean isUserWrite) {
		AccommodationReview accommodationReview = accommodation.getAccommodationReview();

		final List<TagCountDto> countDtoList = accommodationReview == null ? List.of() : accommodationReview.getCountingInfoList().stream()
			.map(tag -> {
				TagSummaryDto summaryDto = TagSummaryDto.of(tag.getCategory());
				return TagCountDto.of(summaryDto, tag.getCnt());
			})
			.collect(Collectors.toList());

		final int userCount = accommodationReview == null ? 0 : accommodationReview.getReviewedUserCnt();

		return AccommodationReviewDto.of(
			countDtoList,
			isUserWrite,
			userCount
		);
	}
}
