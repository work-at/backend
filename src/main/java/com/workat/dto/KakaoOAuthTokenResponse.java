package com.workat.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoOAuthTokenResponse {
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private int expiresIn;
	private String scope;
	private int refreshTokenExpiresIn;
}
