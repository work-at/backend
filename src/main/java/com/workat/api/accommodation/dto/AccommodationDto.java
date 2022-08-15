package com.workat.api.accommodation.dto;

import java.util.List;

import com.workat.domain.tag.dto.TagDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccommodationDto {
	private long id;

	private String name;

	private long price;

	private String imgUrl;

	private List<TagDto> infoTags;

	private List<TagDto> topReviewTags;

	private AccommodationDto(long id, String name, long price, String imgUrl,
		List<TagDto> infoTags, List<TagDto> topReviewTags) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
		this.infoTags = infoTags;
		this.topReviewTags = topReviewTags;
	}

	public static AccommodationDto of(long id, String name, long price, String imgUrl,
		List<TagDto> infoTags, List<TagDto> topReviewTags) {
		return new AccommodationDto(id, name, price, imgUrl,
			infoTags, topReviewTags);
	}
}
