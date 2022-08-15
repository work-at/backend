package com.workat.domain.tag;

import java.util.EnumSet;

import com.workat.common.exception.NotFoundException;

public enum AccommodationInfoTag implements BaseTag {

	WORKSPACE("숙소 내 업무 공간"),
	NEAR_FOREST("숲 인근"),
	NEAR_SEA("바다 인근"),
	NEAR_ATTRACTION("관광지 인근"),
	NEAR_CITY("도시 인근"),
	FREE_PARKING("무료 주차"),
	SHARED_WORKSPACE("공용업무공간");

	private final String content;

	AccommodationInfoTag(String content) {
		this.content = content;
	}

	public static AccommodationInfoTag of(String tagName) {
		return EnumSet.allOf(AccommodationInfoTag.class)
			.stream()
			.filter(tagEnum -> tagEnum.getName().equals(tagName))
			.findFirst()
			.orElseThrow(() -> new NotFoundException(
				String.format("Unsupported type %s", tagName)));
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getContent() {
		return content;
	}
}
