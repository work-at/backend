package com.workat.domain.map.entity;

import com.workat.common.exception.base.BusinessException;

public enum LocationCategory {
	FOOD("FD6"), CAFE("CE7");

	private final String value;

	public String getValue() {
		return value;
	}

	LocationCategory(String value) {
		this.value = value;
	}

	public static LocationCategory of(String category) {
		if (category.equals("food")) {
			return FOOD;
		}

		if (category.equals("cafe")) {
			return CAFE;
		}

		return null;
	}
}
