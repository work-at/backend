package com.workat.domain.tag;

import java.util.EnumSet;
import java.util.Set;

import com.workat.common.exception.NotFoundException;

public enum AccommodationReviewTag implements BaseTag {

	PARKING("주차하기 편해요", "주차하기편해요"),
	VIEW("뷰가 좋아요", "뷰가좋아요"),
	PRICE("가성비가 좋아요", "가성비가좋아요"),
	BED("침대가 편해요", "침대가편해요"),
	CLEAN("방이 청결해요", "방이청결해요"),
	WIFI("와이파이가 빵빵해요", "와이파이빵빵해요"),
	SEAT("책상이 편해요", "책상이편해요"),
	MEAL("조식 먹을 수 있어요", "조식가능해요"),
	LOCATION("교통이 좋아요", "교통이좋아요"),
	POWER("콘센트 자리 많아요", "콘센트많아요");

	public static final Set<AccommodationReviewTag> ALL = EnumSet.allOf(AccommodationReviewTag.class);

	private final String content;

	private final String summary;

	AccommodationReviewTag(String content, String summary) {
		this.content = content;
		this.summary = summary;
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

	public String getSummary() {
		return summary;
	}
}
