package com.workat.common.annotation.aspect;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.workat.api.auth.dto.KakaoOauthTokenInfoResponse;
import com.workat.common.exception.InternalServerException;
import com.workat.common.exception.UnAuthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OauthTokenValidationAspect {

	private static final String ACCESS_TOKEN_INFO_URL = "https://kapi.kakao.com/v1/user/access_token_info";

	private final HttpServletRequest httpServletRequest;

	private final RestTemplate restTemplate;

	@Before(value = "@annotation(com.workat.common.annotation.OauthTokenValidation)")
	public void validate(JoinPoint joinPoint) throws Throwable {
		try {
			String accessToken = httpServletRequest.getHeader("Authorization");
			HttpHeaders headers = new HttpHeaders();

			headers.set("Authorization", accessToken); // Authorization: Bearer ${ACCESS_TOKEN}

			HttpEntity tokenInfoRequest = new HttpEntity(headers);

			ResponseEntity<KakaoOauthTokenInfoResponse> res = requestAccessTokenInfo(tokenInfoRequest);

			log.info(String.format("[Request]  %s", httpServletRequest.getRequestURI()));
			log.info(String.format("[OAuth 2.0] Authentication Success : %s", res.getBody().getId()));
		} catch (Exception e) {
			log.error(String.format("[Exception] : UnAuthorized Token %s", LocalDateTime.now()));
			throw e;
		}
	}

	private ResponseEntity<KakaoOauthTokenInfoResponse> requestAccessTokenInfo(HttpEntity request) {
		try {
			return restTemplate.exchange(ACCESS_TOKEN_INFO_URL, HttpMethod.GET, request,
				KakaoOauthTokenInfoResponse.class);
		} catch (HttpStatusCodeException e) { // FIXME: 에러 안 잡히는 문제 해결하기
			log.error(e.getMessage());

			throw new UnAuthorizedException("oauth error");
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}
	}
}
