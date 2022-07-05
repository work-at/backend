package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpRequest {
	private String nickname;
	private DepartmentType position;
	private DurationType workingYear;

	public SignUpRequest(String nickname, DepartmentType position, DurationType workingYear) {
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
	}

	public static SignUpRequest of(String nickname, DepartmentType position, DurationType workingYear) {
		return new SignUpRequest(nickname, position, workingYear);
	}
}
