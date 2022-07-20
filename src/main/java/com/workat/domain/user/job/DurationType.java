package com.workat.domain.user.job;

public enum DurationType {

	NEW_STAFF("1-2년차"),

	JUNIOR("3-4년차"),

	MID_LEVEL("5-6년차"),

	SENIOR("7-8년차"),

	EXPERT("9-10년차"),

	OVER("11년차~");

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
