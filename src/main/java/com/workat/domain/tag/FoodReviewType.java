package com.workat.domain.tag;

import java.util.EnumSet;

import com.workat.common.exception.NotFoundException;

public enum FoodReviewType implements BaseTag {

	QUICK_FOOD("음식이 빨리\n나와요"),
	EAT_ALONE("혼밥\n가능해요"),
	MUST_GO("맛집이에요"),
	QUIET("조용해요"),
	SNACK("간단히 먹기\n좋아요"),
	VIEW("뷰가\n좋아요"),
	COST("가성비가\n좋아요"),
	PARKING("주차하기\n편해요"),
	SPACE("넓고\n깨끗해요"),
	NOT_CROWDED("사람이 많이\n없어요");

	private final String content;

	FoodReviewType(String content) {
		this.content = content;
	}

	public static FoodReviewType of(String typeName) {
		return EnumSet.allOf(FoodReviewType.class)
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
