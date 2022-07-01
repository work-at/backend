package com.workat.api.user.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpDto {
	private String nickname;
	private String position;
	private int workingYear;

	public SignUpDto(String nickname, String position, int workingYear) {
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
	}

	public static SignUpDto of(String nickname, String position, int workingYear) {
		return new SignUpDto(nickname, position, workingYear);
	}
}
