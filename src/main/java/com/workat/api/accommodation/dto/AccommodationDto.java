package com.workat.api.accommodation.dto;

import com.workat.common.util.UrlUtils;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.tag.dto.TagContentDto;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagSummaryDto;
import java.util.LinkedHashSet;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.dto.TagDto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

	private List<TagDto> topReviewTags;

	@ApiModelProperty(name = "region", example = "SEOUL")
	private RegionType region;

	private AccommodationDto(long id, String name, long price, String imgUrl, List<TagDto> topReviewTags, RegionType region) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
		this.topReviewTags = topReviewTags;
		this.region = region;
	}

	public static AccommodationDto from(long id, String name, long price, String imgUrl,
		List<TagDto> topReviewTags, RegionType region) {
		return new AccommodationDto(id, name, price, imgUrl, topReviewTags, region);
	}
	public static AccommodationDto from(Accommodation accommodation) {
		String imgUrl = UrlUtils.getBaseUrl() + "/uploaded" + accommodation.getThumbnailImgUrl() + ".png";
		List<TagDto> topReviewTags = accommodation.getAccommodationReview().getTopRank(3).stream()
			.map(accommodationReviewCounting -> TagSummaryDto.of(accommodationReviewCounting.getCategory())).collect(Collectors.toList());
		return new AccommodationDto(accommodation.getId(), accommodation.getName(), accommodation.getPrice(), imgUrl, topReviewTags, accommodation.getRegionType());
	}
}
