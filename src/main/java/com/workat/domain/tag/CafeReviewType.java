package com.workat.domain.tag;

import java.util.EnumSet;
import java.util.Set;

import com.workat.common.exception.NotFoundException;

public enum CafeReviewType implements ReviewTag {

	WIFI("와이파이가\n빵빵해요", "와이파이가 빵빵해요", "와이파이빵빵해요"),
	MEAL("식사 메뉴가\n있어요", "식사 메뉴가 있어요", "식사가능해요"),
	QUIET("조용한 공간이\n있어요", "조용한 공간이 있어요", "조용한공간있어요"),
	POWER("콘센트 자리\n많아요", "콘센트 자리 많아요", "콘센트많아요"),
	SEAT("좌석이 업무하기\n좋아요", "좌석이 업무하기 좋아요", "좌석이편해요"),
	VIEW("뷰가\n좋아요", "뷰가 좋아요", "뷰가좋아요"),
	COST("가성비가\n좋아요", "가성비가 좋아요", "가성비가좋아요"),
	PARKING("주차하기\n편해요", "주차하기 편해요", "주차하기편해요"),
	SPACE("넓고\n깨끗해요", "넓고 깨끗해요", "넓고깨끗해요"),
	NOT_CROWDED("사람이 많이\n없어요", "사람이 많이 없어요", "한산해요");

	public static final Set<CafeReviewType> ALL = EnumSet.allOf(CafeReviewType.class);

	private final String content;

	private final String content2;

	private final String summary;

	CafeReviewType(String content, String content2, String summary) {
		this.content = content;
		this.content2 = content2;
		this.summary = summary;
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

	@Override
	public String getContent2() {
		return content2;
	}

	@Override
	public String getSummary() {
		return summary;
	}

}
