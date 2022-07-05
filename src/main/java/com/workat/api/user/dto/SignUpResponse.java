package com.workat.api.user.dto;

import static lombok.AccessLevel.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class SignUpResponse {

	@ApiModelProperty(name = "accessToken", notes = "앱 내 JWT", example = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjU3MDM4NjUxLCJleHAiOjE2NTcwNDIyNTF9.r-nlQ2UygR5pejoeWpnoKddeivHWGZE8QNm-dM6zwQE")
	private String accessToken;

	public SignUpResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public static SignUpResponse of(String accessToken) {
		return new SignUpResponse(accessToken);
	}
}
