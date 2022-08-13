package com.workat.domain.tag;

import java.util.EnumSet;

import com.workat.common.exception.NotFoundException;

public enum CafeReviewType implements BaseTag {

	WIFI("와이파이가\n빵빵해요"),
	MEAL("식사 메뉴가\n있어요"),
	QUIET("조용한 공간이\n있어요"),
	POWER("콘센트 자리\n많아요"),
	SEAT("좌석이 업무하기\n좋아요"),
	VIEW("뷰가\n좋아요"),
	COST("가성비가\n좋아요"),
	PARKING("주차하기\n편해요"),
	SPACE("넓고\n깨끗해요"),
	NOT_CROWDED("사람이 많이\n없어요");

	private final String content;

	CafeReviewType(String content) {
		this.content = content;
	}

	public static CafeReviewType of(String typeName) {
		return EnumSet.allOf(CafeReviewType.class)
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
