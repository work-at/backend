package com.workat.domain.user.job;

public enum DurationType {

	JUNIOR("주니어(1~4년)"),

	MID_LEVEL("미드레벨(5~8년)"),

	SENIOR("시니어(9~12년)"),

	OVER("그 이상(12년 이상)");

	private String type;

	DurationType(String type) {
		this.type = type;
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
