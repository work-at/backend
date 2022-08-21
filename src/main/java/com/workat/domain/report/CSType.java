package com.workat.domain.report;

import java.util.Arrays;
import java.util.List;

public enum CSType {

	INQUERY, SERVICE_OFFER, ERROR_REPORT, OTHERS;

	public static List<CSType> ALL = Arrays.asList(CSType.values());

	public static CSType of(String csType) {
		for (CSType csName : CSType.values()) {
			if (csName.name().equals(csType)) {
				return csName;
			}
		}

		return null;
	}
}
