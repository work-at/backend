package com.workat.api.map.dto;

import java.util.List;

import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewTypeDto;
import com.workat.domain.map.entity.LocationCategory;

import com.workat.domain.tag.dto.TagDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationBriefDto extends LocationDto {

	private LocationCategory category;

	private String placeName;

	private String roadAddressName;

	private String thumbnailImageUrl;

	private int reviewCount;

	private List<TagDto> topReviews;

	@Builder
	public LocationBriefDto(long id, String placeId, double longitude, double latitude, LocationCategory category,
		String placeName, String roadAddressName, String thumbnailImageUrl, int reviewCount,
		List<TagDto> topReviews) {
		super(id, placeId, longitude, latitude);

		this.category = category;
		this.placeName = placeName;
		this.roadAddressName = roadAddressName;
		this.thumbnailImageUrl = thumbnailImageUrl;
		this.reviewCount = reviewCount;
		this.topReviews = topReviews;
	}

}
