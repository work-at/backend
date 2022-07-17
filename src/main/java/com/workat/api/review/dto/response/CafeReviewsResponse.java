package com.workat.api.review.dto.response;

import static lombok.AccessLevel.*;

import java.util.List;

import com.workat.api.review.dto.CafeReviewDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class CafeReviewsResponse {

	@ApiModelProperty(name = "response", notes = "카페 리뷰 리스트")
	private List<CafeReviewDto> cafeReviewDtos;

	private CafeReviewsResponse(List<CafeReviewDto> cafeReviewDtos) {
		this.cafeReviewDtos = cafeReviewDtos;
	}

	public static CafeReviewsResponse of(List<CafeReviewDto> cafeReviewDtos) {
		return new CafeReviewsResponse(cafeReviewDtos);
	}
}
