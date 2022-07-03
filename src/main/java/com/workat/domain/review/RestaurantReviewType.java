package com.workat.domain.review;

public enum RestaurantReviewType implements BaseReviewType {

	PARKING("주차하기 편해요~"),
	VIEW("뷰가 좋아요"),
	NOT_CROWDED("한적해요~"),
	NIGHT_VIEW("야경이 예뻐요~"),
	SPACE("넓고 깨끗해요~"),
	QUICK_FOOD("음식이 빨리 나와요"),
	EAT_ALONE("혼밥하기 좋아요"),
	QUIET("조용해요"),
	DESIGN("인테리어가 예뻐요"),
	SNACK("간단하게 먹기 좋아요");

	private String content;

	RestaurantReviewType(String content) {
		this.content = content;
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
