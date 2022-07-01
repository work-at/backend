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
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.http.dto.KakaoLocalResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocationHttpReceiver {

	private static String KAKAO_LOCAL_BASE_URI = "https://dapi.kakao.com/v2/local";
	private static String KAKAO_CATEGORY_SEARCH_PATH = "/search/category.json";
	private static String KAKAO_CONVERT_TO_ADDRESS_PATH = "/geo/coord2address.json";
	private static String COORDINATE = "WGS84";

	@Value("${external.kakaoOauth.clientId}")
	private String key;

	private final RestTemplate restTemplate;

	public KakaoLocalResponse getLocation(LocationCategory locationCategory, LocationRequest locationRequest) {
		try {
			HttpHeaders headers = getKakaoLocalHeader();
			String uri = convertToUri(KAKAO_LOCAL_BASE_URI + KAKAO_CATEGORY_SEARCH_PATH, locationCategory, locationRequest);
			ResponseEntity<KakaoLocalResponse> response = restTemplate.exchange(
				uri, HttpMethod.GET, new HttpEntity<>(headers), KakaoLocalResponse.class);
			return response.getBody();
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}
	}

	public KakaoAddressResponse getAddress(String longitude, String latitude) {
		try {
			HttpHeaders headers = getKakaoLocalHeader();
			String uri = convertToUri(KAKAO_LOCAL_BASE_URI + KAKAO_CONVERT_TO_ADDRESS_PATH, longitude, latitude);
			ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
				uri, HttpMethod.GET, new HttpEntity<>(headers), KakaoAddressResponse.class);
			return response.getBody();
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}
	}

	private HttpHeaders getKakaoLocalHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "KakaoAK " + key);
		return headers;
	}

	private String convertToUri(String url, String longitude, String latitude) {
		return UriComponentsBuilder.fromHttpUrl(url)
			.queryParam("x", longitude)
			.queryParam("y", latitude)
			.queryParam("input_coord", COORDINATE)
			.toUriString();
	}

	private String convertToUri(String url, LocationCategory locationCategory, LocationRequest locationRequest) {
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
			.queryParam("size", "15")
			.toUriString();
	}

}
