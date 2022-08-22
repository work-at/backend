package com.workat.domain.tour;

import java.util.EnumSet;
import java.util.Set;

public enum RegionCode {

	SEOUL("11"),
	JEJU("50"),
	GANGNEUNG("42150"),
	SOKCHO("42210");

	private String code;

	public static final Set<RegionCode> ALL = EnumSet.allOf(RegionCode.class);

	RegionCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
