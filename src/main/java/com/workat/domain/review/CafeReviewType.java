package com.workat.domain.review;

import java.util.EnumSet;

import com.workat.common.exception.NotFoundException;

public enum CafeReviewType implements BaseReviewType {

	PARKING("주차하기 편해요~"),
	VIEW("뷰가 좋아요"),
	NOT_CROWDED("한적해요~"),
	NIGHT_VIEW("야경이 예뻐요~"),
	SPACE("넓고 깨끗해요~"),
	WIFI("와이파이가 빵빵해요!"),
	POWER("콘센트 자리 많아요!"),
	SEAT("좌석이 편해요"),
	QUIET("조용한 공간이 있어요!"),
	FOCUS("집중이 잘돼요");

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
