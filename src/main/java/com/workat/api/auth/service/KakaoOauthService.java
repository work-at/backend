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

import com.workat.api.auth.dto.KakaoOauthAccessTokenDto;
import com.workat.api.auth.dto.KakaoOauthIdDto;
import com.workat.api.auth.dto.KakaoOauthTokenInfoResponse;
import com.workat.api.auth.dto.KakaoOauthTokenResponse;
import com.workat.common.exception.InternalServerException;
import com.workat.common.exception.base.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {

	// TODO: Value 로
	private static final String REDIRECT_URL = "http://localhost:3000/login"; // TODO: 프론트쪽 리다이렉트 동선 협의되면 수정

	private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";

	private static final String ACCESS_TOKEN_INFO_URL = "https://kapi.kakao.com/v1/user/access_token_info";

	private final RestTemplate restTemplate;

	@Value("${external.kakaoOauth.clientId}")
	private String CLIENT_ID;

	public KakaoOauthAccessTokenDto getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(getAuthParam(code), headers);

		KakaoOauthTokenResponse body = requestAccessToken(tokenRequest);

		return KakaoOauthAccessTokenDto.of(body.getAccessToken());
	}

	public KakaoOauthIdDto getId(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken); // Authorization: Bearer ${ACCESS_TOKEN}

		HttpEntity tokenInfoRequest = new HttpEntity(headers);

		KakaoOauthTokenInfoResponse body = requestAccessTokenInfo(tokenInfoRequest);

		return KakaoOauthIdDto.of(body.getId());
	}

	private KakaoOauthTokenInfoResponse requestAccessTokenInfo(HttpEntity request) {
		try {
			return restTemplate.exchange(ACCESS_TOKEN_INFO_URL, HttpMethod.GET, request,
				KakaoOauthTokenInfoResponse.class).getBody();
		} catch (HttpStatusCodeException e) {
			log.error(e.getMessage());

			throw new BusinessException(e.getStatusCode(), "oauth error");
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

	private KakaoOauthTokenResponse requestAccessToken(HttpEntity request) {
		try {
			return restTemplate.exchange(AUTH_URL, HttpMethod.POST, request, KakaoOauthTokenResponse.class).getBody();
		} catch (HttpStatusCodeException e) {
			log.error(e.getMessage());

			throw new BusinessException(e.getStatusCode(), "oauth error");
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}
	}
}
