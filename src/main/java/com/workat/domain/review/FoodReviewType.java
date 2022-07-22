package com.workat.domain.review;

import java.util.EnumSet;

import com.workat.common.exception.NotFoundException;

public enum FoodReviewType implements BaseReviewType {

	QUICK_FOOD("음식이 빨리\n나와요", "FoodReview1"),
	EAT_ALONE("혼밥\n가능해요", "FoodReview2"),
	MUST_GO("맛집이에요", "FoodReview3"),
	QUIET("조용해요", "FoodReview4"),
	SNACK("간단히 먹기\n좋아요", "FoodReview5"),
	VIEW("뷰가\n좋아요", "CommonReview1"),
	COST("가성비가\n좋아요", "CommonReview2"),
	PARKING("주차하기\n편해요", "CommonReview3"),
	SPACE("넓고\n깨끗해요", "CommonReview4"),
	NOT_CROWDED("사람이 많이\n없어요", "CommonReview5");

	private final String content;
	private final String iconType;

	FoodReviewType(String content, String iconType) {
		this.content = content;
		this.iconType = iconType;
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

	@Override
	public String getIconType() {
		return iconType;
	}
}
