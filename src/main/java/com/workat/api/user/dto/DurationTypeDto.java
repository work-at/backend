package com.workat.api.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DurationTypeDto implements JobTypeDto {

	String name;

	String content;

	String description;

	private DurationTypeDto(String name, String content, String description) {
		this.name = name;
		this.content = content;
		this.description = description;
	}

	public static DurationTypeDto of(String name, String content, String description) {
		return new DurationTypeDto(name, content, description);
	}
}
