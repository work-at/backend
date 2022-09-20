package com.workat.api.accommodation.dto;

import java.util.LinkedHashSet;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.dto.TagDto;

import io.swagger.annotations.ApiModelProperty;
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

	private LinkedHashSet<TagDto> topReviewTags;

	@ApiModelProperty(name = "region", example = "SEOUL")
	private RegionType region;

	private AccommodationDto(long id, String name, long price, String imgUrl, LinkedHashSet<TagDto> topReviewTags, RegionType region) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
		this.topReviewTags = topReviewTags;
		this.region = region;
	}

	public static AccommodationDto of(long id, String name, long price, String imgUrl,
		LinkedHashSet<TagDto> topReviewTags, RegionType region) {
		return new AccommodationDto(id, name, price, imgUrl, topReviewTags, region);
	}
}
