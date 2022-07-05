package com.workat.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workat.api.tour.BigDataLocalResponse;
import com.workat.api.tour.BigDataMetroResponse;
import com.workat.api.tour.model.BigDataResponse;
import com.workat.api.tour.model.BigDataResponseBody;
import com.workat.api.tour.model.BigDataResponseHeader;
import com.workat.api.tour.model.BigDataResponseItem;
import com.workat.api.tour.model.BigDataResponseItems;
import com.workat.common.exception.InternalServerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalApiCallUtils {

	private final RestTemplate restTemplate;

	private static final String TOUR_API_BIGDATA_BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/DataLabService";

	private static final String TOUR_API_BIGDATA_METRO_GOVERNMENT = "/metcoRegnVisitrDDList";

	private static final String TOUR_API_BIGDATA_LOCAL_GOVERNMENT = "/locgoRegnVisitrDDList";

	private static final String MOBILE_OS = "ETC";

	private static final String MOBILE_APP = "WORK_AT";

	private static final String _TYPE = "json";

	private static final ObjectMapper mapper = new ObjectMapper();

	@Value("${external.tour-api.big-data}")
	private String tourApiBigDataToken;

	public BigDataMetroResponse getBigDataMetro(String startYmd, String endYmd, String pageNum, String numOfRows) throws IOException {
		JsonNode responseBody = getJsonNodeResponse(TOUR_API_BIGDATA_METRO_GOVERNMENT, startYmd, endYmd, pageNum, numOfRows);
		return convert(responseBody, BigDataMetroResponse.class);
	}

	public BigDataLocalResponse getBigDataLocal(String startYmd, String endYmd, String pageNum, String numOfRows) throws IOException {
		JsonNode responseBody = getJsonNodeResponse(TOUR_API_BIGDATA_LOCAL_GOVERNMENT, startYmd, endYmd, pageNum, numOfRows);
		return convert(responseBody, BigDataLocalResponse.class);
	}

	private JsonNode getJsonNodeResponse(String path, String startYmd, String endYmd, String pageNum, String numOfRows) {
		HttpHeaders headers = getJsonHeader();
		String uri = convertToUri(path, startYmd, endYmd, pageNum, numOfRows);
		log.info("[관광 API : 요청 URI]" + uri);

		ResponseEntity<JsonNode> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class);

		if (response.getBody() == null) {
			// TODO: 내부 캐싱 값 유지
			log.info("Cannot receive response from Tour Api");
		}
		return response.getBody();
	}

	private HttpHeaders getJsonHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String convertToUri(String path, String startYmd, String endYmd, String pageNum, String numOfRows) {
		String url = TOUR_API_BIGDATA_BASE_URL + path;
		return UriComponentsBuilder.fromHttpUrl(url)
			.queryParam("serviceKey", tourApiBigDataToken)
			.queryParam("MobileOS", MOBILE_OS)
			.queryParam("MobileApp", MOBILE_APP)
			.queryParam("numOfRows", numOfRows)
			.queryParam("startYmd", startYmd)
			.queryParam("pageNo", pageNum)
			.queryParam("endYmd", endYmd)
			.queryParam("_type", _TYPE)
			.build()
			.toUriString();
	}

	private <T> T convert(JsonNode jsonNode, Class<T> clazz) throws IOException {
		JsonNode response = jsonNode.get("response");
		JsonNode header = response.get("header");
		if (!header.get("resultMsg").asText().equals("OK")) {
			throw new InternalServerException("tour api error");
		}
		BigDataResponseHeader bigDataResponseHeader = mapper.treeToValue(header, BigDataResponseHeader.class);

		JsonNode body = response.get("body");
		JsonNode items = body.get("items");
		List<BigDataResponseItem> bigDataResponseItemList = new ArrayList<>();
		if (items.size() != 0) {
			JsonNode item = items.get("item");

			if (item.isArray()) {
				bigDataResponseItemList.addAll(mapper.readerFor(new TypeReference<List<BigDataResponseItem>>() {
				}).readValue(item));
			} else {
				BigDataResponseItem bigDataResponseItem = mapper.treeToValue(items.get("item"), BigDataResponseItem.class);
				bigDataResponseItemList.add(bigDataResponseItem);
			}
		}

		BigDataResponseItems bigDataResponseItems = BigDataResponseItems.of(bigDataResponseItemList);

		int numOfRows = body.get("numOfRows").asInt();
		int pageNo = body.get("pageNo").asInt();
		int totalCount = body.get("totalCount").asInt();
		BigDataResponseBody bigDataResponseBody = BigDataResponseBody.of(bigDataResponseItems, numOfRows, pageNo, totalCount);

		BigDataResponse bigDataResponse = BigDataResponse.of(bigDataResponseHeader, bigDataResponseBody);

		if (clazz == BigDataMetroResponse.class) {
			BigDataMetroResponse bigDataMetroResponse = BigDataMetroResponse.of(bigDataResponse);
			return clazz.cast(bigDataMetroResponse);
		} else if (clazz == BigDataLocalResponse.class) {
			BigDataLocalResponse bigDataLocalResponse = BigDataLocalResponse.of(bigDataResponse);
			return clazz.cast(bigDataLocalResponse);
		}
		throw new InternalServerException("convert class type error");
	}
}
