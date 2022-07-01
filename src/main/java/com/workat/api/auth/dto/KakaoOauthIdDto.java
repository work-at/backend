package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOauthIdDto {

	private long id;

	private KakaoOauthIdDto(long id) {
		this.id = id;
	}

	public static KakaoOauthIdDto of(long id) {
		return new KakaoOauthIdDto(id);
	}
}
