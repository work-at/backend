package com.workat.api.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DepartmentTypeResponse implements JobTypeResponse {

	String name;

	String content;

	private DepartmentTypeResponse(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static DepartmentTypeResponse of(String name, String content) {
		return new DepartmentTypeResponse(name, content);
	}
}
