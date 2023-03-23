package com.workat.domain.map.entity;

import com.workat.common.convert.ConvertContent;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.tag.review.CafeReviewTag;
import com.workat.domain.tag.review.RestaurantReviewTag;
import com.workat.domain.tag.review.ReviewTag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LocationType implements ConvertContent {
	RESTAURANT("FD6"), CAFE("CE7");

	private final String value;

	public List<? extends ReviewTag> getAllContents() {
		if (this == CAFE) {
			return CafeReviewTag.ALL;
		} else if (this == RESTAURANT) {
			return RestaurantReviewTag.ALL;
		} else {
			throw new NotFoundException("no matched contents (value: " + this.name() + ")");
		}
	}
}
