package com.workat.domain.map.entity;

public enum LocationCategory {
	RESTAURANT("FD6"), CAFE("CE7");

	private final String value;

	public String getValue() {
		return value;
	}

	LocationCategory(String value) {
		this.value = value;
	}
}
