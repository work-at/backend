package com.workat.api.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DepartmentTypeDto implements JobTypeDto {

	String name;

	String content;

	private DepartmentTypeDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static DepartmentTypeDto of(String name, String content) {
		return new DepartmentTypeDto(name, content);
	}
}
