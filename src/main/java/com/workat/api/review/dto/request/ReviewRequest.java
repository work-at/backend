package com.workat.api.review.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.workat.common.exception.ConflictException;
import com.workat.domain.map.entity.LocationType;
import com.workat.domain.tag.review.ReviewTag;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ReviewRequest {

	private LocationType category;

	@JsonProperty("reviewTypeNames") // JsonProperty 협의 후 삭제할것
	@ApiModelProperty(name = "reviewTypeNames", notes = "추가할 리뷰 타입 이름", example = "[PARKING, VIEW]")
	private List<ReviewTag> reviewTags;

	public void validate() {
		boolean anyMatch = reviewTags.stream()
			.anyMatch(tag -> this.category.getAllContents().contains(tag));

		if (anyMatch) {
			throw new ConflictException("no match category & reviewTags");
		}
	}

	public static ReviewRequest of(LocationType category, List<ReviewTag> reviewTags) {
		return new ReviewRequest(category, reviewTags);
	}
}
