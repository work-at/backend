package com.workat.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoOAuthAccessToken {
	private final String accessToken;
}
