package com.workat.domain.map.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workat.api.map.dto.LocationRequest;
import com.workat.common.exception.InternalServerException;
import com.workat.common.exception.base.BusinessException;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.dto.KakaoLocalResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocationHttpReceiver {

	private static String KAKAO_LOCAL_URI = "https://dapi.kakao.com/v2/local/search/category.json";

	@Value("${external.kakaoOauth.clientId}")
	private String key;

	private final RestTemplate restTemplate;

	public KakaoLocalResponse getLocation(LocationCategory locationCategory, LocationRequest locationRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "KakaoAK " + key);

		try {
			ResponseEntity<KakaoLocalResponse> response = restTemplate.exchange(
				convertUrl(KAKAO_LOCAL_URI, locationCategory, locationRequest),
				HttpMethod.GET, new HttpEntity<>(headers), KakaoLocalResponse.class);
			return response.getBody();
		} catch (HttpStatusCodeException e) {
			throw new BusinessException(e.getStatusCode(), e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}
	}

	private String convertUrl(String url, LocationCategory locationCategory, LocationRequest locationRequest) {
		String category = locationCategory.getValue();
		float x = locationRequest.getX();
		float y = locationRequest.getY();
		int radius = locationRequest.getRadius();
		int page = locationRequest.getPage();

		return UriComponentsBuilder.fromHttpUrl(url)
			.queryParam("category_group_code", "")
			.queryParam("x", String.valueOf(x))
			.queryParam("y", String.valueOf(y))
			.queryParam("radius", String.valueOf(radius))
			.queryParam("page", String.valueOf(page))
			.queryParam("size", "15").toUriString();
	}

}
