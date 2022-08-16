package com.workat.domain.accommodation;

import lombok.Getter;

@Getter
public enum RegionType {

	SEOUL("Seoul"),
	JEJU("Jeju"),
	GANGNEUNG("Gangneung"),
	SOKCHO("Sokcho");

	private final String value;

	RegionType(String value) {
		this.value = value;
	}
}
