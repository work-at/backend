package com.workat.api.accommodation.dto;

import com.workat.domain.accommodation.RegionType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccommodationCuration {

	private long id;

	private String name;

	private RegionType region;

	private String imgUrl;

	private AccommodationCuration(long id, String name, RegionType region, String imgUrl) {
		this.id = id;
		this.name = name;
		this.region = region;
		this.imgUrl = imgUrl;
	}

	public static AccommodationCuration of(long id, String name, RegionType region, String imgUrl) {
		return new AccommodationCuration(id, name, region, imgUrl);
	}
}
