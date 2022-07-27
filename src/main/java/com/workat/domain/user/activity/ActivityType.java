package com.workat.domain.user.activity;

public enum ActivityType {

	JUNIOR("주니어모여라"),
	SENIOR("시니어모여라"),
	DESIGN("디자인같이해요"),
	DEVELOP("개발같이해요"),
	PLAN("기획같이해요"),
	IT("IT함께해요"),
	FINANCIAL("금융함께해요"),
	COMMERCE("이커머스함께해요"),
	BUSINESS("제조업함께해요"),
	STARTUP("스타트업함께해요"),
	PASSION("열정맨"),
	TALK("직무토크하실분"),
	CAREER("진로고민"),
	DINNER("저녁메이트구해요"),
	LUNCH("점심메이트구해요"),
	AFTER_WORK("퇴근후함께놀아요");

	private String type;

	ActivityType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static ActivityType of(String activity) {
		for (ActivityType activityType : ActivityType.values()) {
			if (activityType.name().equals(activity)) {
				return activityType;
			}
		}

		return null;
	}
}
