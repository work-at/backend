package com.workat.domain.tag;

import java.util.EnumSet;
import java.util.Set;

import com.workat.common.exception.NotFoundException;

public enum AccommodationReviewTag implements BaseTag {

	WIFI("와이파이가 빵빵해요"),
	POWER("콘센트 자리 많아요"),
	SEAT("좌석이 업무하기 좋아요"),
	QUIET("조용한 공간이 있어요"),
	FOCUS("집중이 잘 돼요"),
	SERVE_MEAL("식사메뉴가 있어요");

	public static final Set<AccommodationReviewTag> ALL = EnumSet.allOf(AccommodationReviewTag.class);

	private final String content;

	AccommodationReviewTag(String content) {
		this.content = content;
	}

	public static AccommodationReviewTag of(String typeName) {
		return EnumSet.allOf(AccommodationReviewTag.class)
			.stream()
			.filter(typeEnum -> typeEnum.getName().equals(typeName))
			.findFirst()
			.orElseThrow(() -> new NotFoundException(
				String.format("Unsupported type %s", typeName)));
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
