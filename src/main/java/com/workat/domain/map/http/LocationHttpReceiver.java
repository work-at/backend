package com.workat.domain.map.http;

import java.util.ArrayList;
import java.util.List;

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

import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.common.exception.InternalServerException;
import com.workat.common.exception.base.BusinessException;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.http.dto.KakaoLocalResponse;
import com.workat.domain.map.vo.MapPoint;

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

	public List<KakaoLocalDataDto> updateLocations(LocationCategory category, MapPoint point, int radius) {
		ArrayList<KakaoLocalDataDto> result = new ArrayList<>();

		try {
			HttpHeaders headers = getKakaoLocalHeader();
			String url = KAKAO_LOCAL_BASE_URI + KAKAO_CATEGORY_SEARCH_PATH;
			int pageCount = 0;
			while (true) {
				String uri = convertToUri(url, category, point, radius, ++pageCount);

				KakaoLocalResponse response = restTemplate.exchange(uri, HttpMethod.GET,
					new HttpEntity<>(headers), KakaoLocalResponse.class).getBody();
				List<KakaoLocalDataDto> data = response.getDocuments();
				log.info("get local data from kakao success : page count {}, data size {}", (pageCount), data.size());
				result.addAll(data);

				if (response.getMeta().isEnd() || response.getMeta().getPageableCount() == pageCount) {
					log.info("get local data from kakao stop");
					break;
				}

			}
		} catch (HttpStatusCodeException e) {
			throw new BusinessException(e.getStatusCode(), e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalServerException(e.getMessage());
		}

		return result;
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

	private String convertToUri(String url, LocationCategory category, MapPoint point, int radius, int page) {
		String categoryCode = category.getValue();
		double longitude = point.getLongitude();
		double latitude = point.getLatitude();

		return UriComponentsBuilder.fromHttpUrl(url)
			.queryParam("category_group_code", categoryCode)
			.queryParam("x", String.valueOf(longitude))
			.queryParam("y", String.valueOf(latitude))
			.queryParam("radius", String.valueOf(radius))
			.queryParam("page", String.valueOf(page))
			.queryParam("size", "15")
			.queryParam("sort", "accuracy").toUriString();
	}
}
