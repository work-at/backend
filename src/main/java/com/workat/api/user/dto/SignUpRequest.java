package com.workat.api.user.dto;

import static lombok.AccessLevel.*;

import com.workat.domain.auth.OauthType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpRequest {
	private OauthType oauthType;
	private long oauthId;
	private String nickname;
	private String position;
	private int workingYear;

	public SignUpRequest(OauthType oauthType, long oauthId, String nickname, String position, int workingYear) {
		this.oauthId = oauthId;
		this.oauthType = oauthType;
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
	}

	public static SignUpRequest of(OauthType oauthType, long oauthId, String nickname, String position,
		int workingYear) {
		return new SignUpRequest(oauthType, oauthId, nickname, position, workingYear);
	}
}
