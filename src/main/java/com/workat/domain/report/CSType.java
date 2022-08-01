package com.workat.domain.report;

public enum CSType {

	INQUERY, SERVICE_OFFER, ERROR_REPORT, OTHERS;

	public static CSType of(String csType) {
		for (CSType csName : CSType.values()) {
			if (csName.name().equals(csType)) {
				return csName;
			}
		}

		return null;
	}
}
