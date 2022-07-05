package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOauthTokenRequest {

	@ApiModelProperty(name = "code", notes = "토큰 받기 요청에 필요한 인가 코드", example = "XDvDqeu3ARHPSF7jRt7yBFeD0jjb1Vp3sX9AvDwka4sPe7zozGYi5jrlxU5kiCzJFdmh_wo9c5oAAAGBzw_AQw")
	private String code;

	private KakaoOauthTokenRequest(String code) {
		this.code = code;
	}

	public static KakaoOauthTokenRequest of(String code) {
		return new KakaoOauthTokenRequest(code);
	}
}
