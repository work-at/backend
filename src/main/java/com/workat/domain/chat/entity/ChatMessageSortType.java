package com.workat.domain.chat.entity;

public enum ChatMessageSortType {
	BEFORE, AFTER;

	public static ChatMessageSortType of(String name) {
		if (name == null) {
			throw new RuntimeException("chat message sort type not match, name value = " + name);
		}

		name = name.toUpperCase();

		for (ChatMessageSortType type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}

		throw new RuntimeException("chat message sort type not match, name value = " + name);
	}
}
