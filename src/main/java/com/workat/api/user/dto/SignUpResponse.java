package com.workat.api.user.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpResponse {

	private String accessToken;

	public SignUpResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public static SignUpResponse of(String accessToken) {
		return new SignUpResponse(accessToken);
	}
}
