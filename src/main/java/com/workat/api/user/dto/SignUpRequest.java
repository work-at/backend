package com.workat.api.user.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpRequest {
	private String nickname;
	private String position;
	private int workingYear;

	public SignUpRequest(String nickname, String position, int workingYear) {
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
	}

	public static SignUpRequest of(String nickname, String position, int workingYear) {
		return new SignUpRequest(nickname, position, workingYear);
	}
}
