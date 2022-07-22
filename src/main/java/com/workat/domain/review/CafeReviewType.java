package com.workat.domain.review;

import java.util.EnumSet;

import com.workat.common.exception.NotFoundException;

public enum CafeReviewType implements BaseReviewType {

	VIEW("뷰가 좋아요"),
	COST("가성비가 좋아요"),
	PARKING("주차하기 편해요"),
	SPACE("넓고 깨끗해요"),
	NOT_CROWDED("사람이 많이 없어요"),
	WIFI("와이파이가 빵빵해요"),
	MEAL("식사 메뉴가 있어요"),
	QUIET("조용한 공간이 있어요"),
	POWER("콘센트 자리 많아요"),
	SEAT("좌석이 업무하기 좋아요");

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
