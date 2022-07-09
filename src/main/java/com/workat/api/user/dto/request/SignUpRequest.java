package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import com.workat.domain.auth.OauthType;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpRequest {

	@ApiModelProperty(name = "oauthType", notes = "oauth 로그인 타입", example = "\"KAKAO\"")
	private OauthType oauthType;

	@ApiModelProperty(name = "oauthId", notes = "카카오 유저 식별자", example = "2329023349")
	private long oauthId;

	@ApiModelProperty(name = "nickname", notes = "유저 닉네임", example = "\"나는스벅라떼좋아\"")
	private String nickname;

	@ApiModelProperty(name = "position", notes = "직무 타입", example = "\"IT_ENGINEER\"")
	private DepartmentType position;

	@ApiModelProperty(name = "workingYear", notes = "연차 타입", example = "\"JUNIOR\"")
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
