package com.workat.api.accommodation.dto;

import com.workat.common.util.UrlUtils;
import com.workat.domain.accommodation.RegionType;

import com.workat.domain.accommodation.entity.Accommodation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccommodationCurationDto {

	private long id;

	private String name;

	private RegionType region;

	private String imgUrl;

	private AccommodationCurationDto(long id, String name, RegionType region, String imgUrl) {
		this.id = id;
		this.name = name;
		this.region = region;
		this.imgUrl = imgUrl;
	}

	public static AccommodationCurationDto of(long id, String name, RegionType region, String imgUrl) {
		return new AccommodationCurationDto(id, name, region, imgUrl);
	}

	public static AccommodationCurationDto of(Accommodation accommodation) {
		String baseUrl = UrlUtils.getBaseUrl();
		return AccommodationCurationDto.of(
			accommodation.getId(),
			accommodation.getName(),
			accommodation.getRegionType(),
			baseUrl + "/uploaded" + accommodation.getThumbnailImgUrl() + ".png");
	}
}
