package com.workat.api.accommodation.dto.response;

import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationReviewDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccommodationResponse {

	private AccommodationDetailDto accommodationDetail;

	private AccommodationReviewDto AccommodationReview;

	private AccommodationResponse(AccommodationDetailDto accommodationDetail,
		AccommodationReviewDto accommodationReview) {
		this.accommodationDetail = accommodationDetail;
		AccommodationReview = accommodationReview;
	}

	public static AccommodationResponse of(AccommodationDetailDto accommodationDetail,
		AccommodationReviewDto accommodationReview) {
		return new AccommodationResponse(accommodationDetail, accommodationReview);
	}
}
