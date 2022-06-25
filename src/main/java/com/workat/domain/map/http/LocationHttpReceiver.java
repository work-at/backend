package com.workat.domain.map.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

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
	private final String key;

	private final RestTemplate restTemplate;

	public KakaoLocalResponse getLocation(LocationCategory locationCategory, LocationRequest locationRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "KakaoAK " + key);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
			getParams(locationCategory, locationRequest), headers);

		try {
			return restTemplate.exchange(KAKAO_LOCAL_URI, HttpMethod.GET, request, KakaoLocalResponse.class).getBody();
		} catch (HttpStatusCodeException e) {
			throw new BusinessException(e.getStatusCode(), e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}
	}

	private MultiValueMap<String, String> getParams(LocationCategory locationCategory,
		LocationRequest locationRequest) {
		String category = locationCategory.getValue();
		float x = locationRequest.getX();
		float y = locationRequest.getY();
		int radius = locationRequest.getRadius();
		int page = locationRequest.getPage();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("category_group_code", category);
		params.add("x", String.valueOf(x));
		params.add("y", String.valueOf(y));
		params.add("radius", String.valueOf(radius));
		params.add("page", String.valueOf(page));
		params.add("size", "15");
		params.add("sort", "distance");
		return params;
	}

}
