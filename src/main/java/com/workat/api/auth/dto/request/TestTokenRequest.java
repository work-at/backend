package com.workat.api.auth.dto.request;

import static lombok.AccessLevel.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class TestTokenRequest {

	@ApiModelProperty(name = "code", notes = "테스트 토큰 받기 요청에 필요한 코드", example = "tour1")
	private String code;

	private TestTokenRequest(String code) {
		this.code = code;
	}

	public static TestTokenRequest of(String code) {
		return new TestTokenRequest(code);
	}
}
