package com.workat.api.accommodation.dto.request;

import com.workat.domain.tag.review.AccommodationReviewTag;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccommodationReviewRequest {

	@ApiModelProperty(name = "tagNames", notes = "추가할 리뷰 태그", example = "[WIFI, POWER]")
	private List<AccommodationReviewTag> tags;

	private AccommodationReviewRequest(List<AccommodationReviewTag> tags) {
		this.tags = tags;
	}

	public static AccommodationReviewRequest of(List<AccommodationReviewTag> tags) {
		return new AccommodationReviewRequest(tags);
	}
}
