package com.workat.domain.tour;

import java.util.EnumSet;
import java.util.Set;

public enum RegionCode {

	SEOUL("11", "/metcoRegnVisitrDDList"),
	JEJU("50", "/metcoRegnVisitrDDList"),
	GANGNEUNG("42150", "/locgoRegnVisitrDDList"),
	SOKCHO("42210", "/locgoRegnVisitrDDList");

	private String code;
	private String endpoint;

	public static final Set<RegionCode> ALL = EnumSet.allOf(RegionCode.class);

	RegionCode(String code, String endpoint) {
		this.code = code;
		this.endpoint = endpoint;
	}

	public String getCode() {
		return code;
	}
}
