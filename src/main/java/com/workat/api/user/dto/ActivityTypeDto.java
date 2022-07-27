package com.workat.api.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ActivityTypeDto implements UserTypeDto {

	String name;

	String content;

	private ActivityTypeDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static ActivityTypeDto of(String name, String content) {
		return new ActivityTypeDto(name, content);
	}
}
