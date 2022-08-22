package com.workat.domain.accommodation;

import java.util.List;
import java.util.Random;

import lombok.Getter;

@Getter
public enum RegionType {

	SEOUL("Seoul", "11"),
	JEJU("Jeju", "50"),
	GANGNEUNG("Gangneung", "42150"),
	SOKCHO("Sokcho", "42210");

	private final String value;
	private final String code;
	private static final List<RegionType> ALL = List.of(values());
	private static final int SIZE = ALL.size();
	private static final Random RANDOM = new Random();

	RegionType(String value, String code) {
		this.value = value;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static RegionType getRandom() {
		return ALL.get(RANDOM.nextInt(SIZE));
	}
}
