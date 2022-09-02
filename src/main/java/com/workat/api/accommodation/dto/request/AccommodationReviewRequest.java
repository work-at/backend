package com.workat.api.accommodation.dto.request;

import java.util.HashSet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccommodationReviewRequest {

	@ApiModelProperty(name = "tagNames", notes = "추가할 리뷰 태그", example = "[WIFI, POWER]")
	private HashSet<String> tagNames;

	private AccommodationReviewRequest(HashSet<String> tagNames) {
		this.tagNames = tagNames;
	}

	public static AccommodationReviewRequest of(HashSet<String> tagNames) {
		return new AccommodationReviewRequest(tagNames);
	}
}
