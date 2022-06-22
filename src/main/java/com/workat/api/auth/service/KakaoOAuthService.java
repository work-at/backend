package com.workat.api.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.workat.api.auth.dto.KakaoOAuthAccessTokenDto;
import com.workat.api.auth.dto.KakaoOAuthTokenResponse;
import com.workat.common.exception.InternalServerException;
import com.workat.common.exception.base.BusinessException;

@Service
public class KakaoOAuthService {
	private static final String REDIRECT_URL = "http://localhost:3000/login"; // TODO: 프론트쪽 리다이렉트 동선 협의되면 수정
	private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";

	@Value("${external.kakaoOauth.clientId}")
	private String CLIENT_ID;

	public KakaoOAuthAccessTokenDto auth(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(getAuthParam(code), headers);

		KakaoOAuthTokenResponse body = requestAuth(kakaoTokenRequest);

		return KakaoOAuthAccessTokenDto.of(body.getAccessToken());
	}

	private KakaoOAuthTokenResponse requestAuth(HttpEntity request) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			return restTemplate.exchange(AUTH_URL, HttpMethod.POST, request, KakaoOAuthTokenResponse.class)
				.getBody();
		} catch (HttpStatusCodeException e) {
			System.out.println(e.getMessage()); // TODO: 로깅 처리 필요

			throw new BusinessException(e.getStatusCode(), "login error");
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
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
