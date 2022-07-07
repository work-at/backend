package com.workat.api.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DurationTypeDto implements JobTypeDto {

	String name;

	String content;

	private DurationTypeDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static DurationTypeDto of(String name, String content) {
		return new DurationTypeDto(name, content);
	}
}
