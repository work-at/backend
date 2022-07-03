package com.workat.api.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DurationTypeResponse implements JobTypeResponse {

	String name;

	String content;

	String description;

	private DurationTypeResponse(String name, String content, String description) {
		this.name = name;
		this.content = content;
		this.description = description;
	}

	public static DurationTypeResponse of(String name, String content, String description) {
		return new DurationTypeResponse(name, content, description);
	}
}
