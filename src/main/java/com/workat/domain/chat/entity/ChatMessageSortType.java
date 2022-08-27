package com.workat.domain.chat.entity;

public enum ChatMessageSortType {
	BEFORE, AFTER, INIT;

	public static ChatMessageSortType of(String name) {
		if (name == null) {
			return INIT;
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
