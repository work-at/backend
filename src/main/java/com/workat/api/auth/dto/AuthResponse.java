package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import com.workat.domain.auth.AuthCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuthResponse {

	@ApiModelProperty(name = "code", notes = "결과 코드(WORK01:로그인 성공, WORK02:로그인 실패(회원가입))", example = "WORK01")
	private AuthCode code;

	@ApiModelProperty(name = "accessToken", notes = "카카오 토큰", example = "AMIoqoP83hO99P3aI8lnxA74ffgx4iQUr0dgEQirCilwEwAAAYHPFZGB")
	private String accessToken;

	@ApiModelProperty(name = "ouathId", notes = "카카오 유저 식별자", example = "2329023349")
	private long ouathId;

	private AuthResponse(AuthCode code, long ouathId) {
		this.code = code;
		this.ouathId = ouathId;
	}

	private AuthResponse(AuthCode code, String accessToken) {
		this.code = code;
		this.accessToken = accessToken;
	}

	public static AuthResponse ResponseForLogin(String accessToken) {
		return new AuthResponse(AuthCode.WORK01, accessToken);
	}

	public static AuthResponse ResponseForSignup(long ouathId) {
		return new AuthResponse(AuthCode.WORK02, ouathId);
	}

}
