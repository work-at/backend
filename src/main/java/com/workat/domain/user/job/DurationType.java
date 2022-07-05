package com.workat.domain.user.job;

public enum DurationType {

	JUNIOR("주니어", "1~4년"),

	MID_LEVEL("미드레벨", "5~8년"),

	SENIOR("시니어", "9~12년"),

	OVER("그 이상", "12년 이상");

	private String type;
	private String description;

	DurationType(String type, String description) {
		this.type = type;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public static DurationType of(String duration) {
		for (DurationType durationType : DurationType.values()) {
			if (durationType.name().equals(duration)) {
				return durationType;
			}
		}

		return null;
	}
}
