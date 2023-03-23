package com.workat.domain.tag.review;

import java.util.EnumSet;
import java.util.List;

import com.workat.common.exception.NotFoundException;

public enum RestaurantReviewTag implements ReviewTag {

	QUICK_FOOD("음식이 빨리\n나와요", "음식이 빨리 나와요", "음식빨리나와요"),
	EAT_ALONE("혼밥\n가능해요", "혼밥 가능해요", "혼밥가능해요"),
	MUST_GO("맛집이에요", "맛집이에요", "맛집이에요"),
	QUIET("조용해요", "조용해요", "조용해요"),
	SNACK("간단히 먹기\n좋아요", "간단히 먹기 좋아요", "간편해요"),
	VIEW("뷰가\n좋아요", "뷰가 좋아요", "뷰가좋아요"),
	COST("가성비가\n좋아요", "가성비가 좋아요", "가성비가좋아요"),
	PARKING("주차하기\n편해요", "주차하기 편해요", "주차하기편해요"),
	SPACE("넓고\n깨끗해요", "넓고 깨끗해요", "넓고깨끗해요"),
	NOT_CROWDED("사람이 많이\n없어요", "사람이 많이없어요", "한산해요");

	public static final List<RestaurantReviewTag> ALL = List.of(RestaurantReviewTag.values());

	private final String content;

	private final String content2;

	private final String summary;

	RestaurantReviewTag(String content, String content2, String summary) {
		this.content = content;
		this.content2 = content2;
		this.summary = summary;
	}

	public static RestaurantReviewTag of(String typeName) {
		return EnumSet.allOf(RestaurantReviewTag.class)
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
