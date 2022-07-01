package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoOauthTokenInfoResponse {

	private long id;

	private int expiresIn;

	private int appId;

	public KakaoOauthTokenInfoResponse(long id, int expiresIn, int appId) {
		this.id = id;
		this.expiresIn = expiresIn;
		this.appId = appId;
	}

	public static KakaoOauthTokenInfoResponse of(long id, int expiresIn, int appId) {
		return new KakaoOauthTokenInfoResponse(id, expiresIn, appId);
	}
}
