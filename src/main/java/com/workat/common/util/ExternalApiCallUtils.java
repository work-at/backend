package com.workat.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		String url = TOUR_API_BIGDATA_BASE_URL + TOUR_API_BIGDATA_METRO_GOVERNMENT;
		String uri = UriComponentsBuilder.fromHttpUrl(url)
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
		log.info(uri);

		HttpEntity<?> entity = new HttpEntity<>(headers);
		HttpEntity<JsonNode> response = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class);
		return convert(Objects.requireNonNull(response.getBody()), BigDataMetroResponse.class);
	}

	public BigDataLocalResponse getBigDataLocal(String startYmd, String endYmd, String pageNum, String numOfRows) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		String url = TOUR_API_BIGDATA_BASE_URL + TOUR_API_BIGDATA_LOCAL_GOVERNMENT;
		String uri = UriComponentsBuilder.fromHttpUrl(url)
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
		log.info(uri);

		HttpEntity<?> entity = new HttpEntity<>(headers);
		HttpEntity<JsonNode> response = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class);
		return convert(Objects.requireNonNull(response.getBody()), BigDataLocalResponse.class);
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

		BigDataResponseItems bigDataResponseItems = BigDataResponseItems.builder()
			.item(bigDataResponseItemList)
			.build();

		BigDataResponseBody bigDataResponseBody = BigDataResponseBody
			.builder()
			.items(bigDataResponseItems)
			.numOfRows(body.get("numOfRows").asInt())
			.totalCount(body.get("totalCount").asInt())
			.pageNo(body.get("pageNo").asInt())
			.build();

		BigDataResponse bigDataResponse = BigDataResponse
			.builder()
			.header(bigDataResponseHeader)
			.body(bigDataResponseBody)
			.build();

		if (clazz == BigDataMetroResponse.class) {
			BigDataMetroResponse bigDataMetroResponse = BigDataMetroResponse.builder()
				.response(bigDataResponse)
				.build();
			return clazz.cast(bigDataMetroResponse);
		} else if (clazz == BigDataLocalResponse.class) {
			BigDataLocalResponse bigDataLocalResponse = BigDataLocalResponse.builder()
				.response(bigDataResponse)
				.build();
			return clazz.cast(bigDataLocalResponse);
		}
		throw new InternalServerException("convert class type error");
	}
}
