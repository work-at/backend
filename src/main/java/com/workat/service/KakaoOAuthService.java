package com.workat.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.workat.dto.KakaoOAuthAccessToken;
import com.workat.dto.KakaoOAuthTokenResponse;

@Service
public class KakaoOAuthService {
	private static final String CLIENT_ID = "7052acd04b3385c80fac9bb40d8b5a32";
	private static final String REDIRECT_URL = "http://localhost:3000/login"; // TODO: 프론트쪽 리다이렉트 동선 협의되면 수정
	private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";

	public KakaoOAuthAccessToken auth(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(getAuthParam(code), headers);

		KakaoOAuthTokenResponse body = requestAuth(kakaoTokenRequest);

		return new KakaoOAuthAccessToken(body.getAccessToken());
	}

	private KakaoOAuthTokenResponse requestAuth(HttpEntity request) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			return restTemplate
				.exchange(AUTH_URL, HttpMethod.POST, request, KakaoOAuthTokenResponse.class)
				.getBody();
		} catch (Exception e) {
			throw e;
		}
	}

	private MultiValueMap<String, String> getAuthParam(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", CLIENT_ID);
		params.add("redirect_uri", REDIRECT_URL);
		params.add("code", code);

		return params;
	}
}
