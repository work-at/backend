package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpRequest {
	private OauthType oauthType;
	private long oauthId;
	private String nickname;
	private DepartmentType position;
	private DurationType workingYear;

	public SignUpRequest(OauthType oauthType, long oauthId, String nickname, DepartmentType position,
		DurationType workingYear) {
		this.oauthType = oauthType;
		this.oauthId = oauthId;
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
	}

	public static SignUpRequest of(OauthType oauthType, long oauthId, String nickname, DepartmentType position,
		DurationType workingYear) {
		return new SignUpRequest(oauthType, oauthId, nickname, position, workingYear);
	}
}
