package com.workat.common.util;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.workat.api.tour.BigDataLocalResponse;
import com.workat.api.tour.BigDataMetroResponse;

@ExtendWith(MockitoExtension.class)
public class ExternalApiCallUtilsTest {

	@Mock
	private RestTemplate restTemplate;

	private ExternalApiCallUtils externalApiCallUtils;

	private final ObjectMapper mapper = new ObjectMapper();

	private final String token = "2scFfN6uVjhuRcXrj1DgGjslYn0wYJc7kvCOHHwOI/0kr1sjf2OqLLjq5SvCt3jKyr4JPHr/K3QGuXhvv6HYlQ==";

	private ObjectNode bigDataMetroItem, header;

	@BeforeEach
	public void setUp() {
		externalApiCallUtils = new ExternalApiCallUtils(restTemplate);
		Field field = ReflectionUtils.findField(externalApiCallUtils.getClass(), "tourApiBigDataToken");
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, externalApiCallUtils, token);

		bigDataMetroItem = mapper.createObjectNode();
		bigDataMetroItem.put("areaCode", 11);
		bigDataMetroItem.put("areaNm", "서울특별시");
		bigDataMetroItem.put("baseYmd", 20210625);
		bigDataMetroItem.put("daywkDivCd", 5);
		bigDataMetroItem.put("daywkDivNm", "금요일");
		bigDataMetroItem.put("touDivCd", 1);
		bigDataMetroItem.put("touDivNm", "현지인(a)");
		bigDataMetroItem.put("touNum", 4976384);

		header = mapper.createObjectNode();
		header.put("resultCode", "0000");
		header.put("resultMsg", "OK");
	}

	@Test
	void getBigDataMetro_get_multiple_success_test() throws IOException {
		//given
		ArrayNode itemList = mapper.createArrayNode();
		itemList.add(bigDataMetroItem);
		itemList.add(bigDataMetroItem);

		ObjectNode item = mapper.createObjectNode();
		item.set("item", itemList);

		ObjectNode body = mapper.createObjectNode();
		body.set("items", item);
		body.put("numOfRows", 2);
		body.put("pageNo", 1);
		body.put("totalCount", 51);

		ObjectNode response = mapper.createObjectNode();
		response.set("header", header);
		response.set("body", body);

		ObjectNode responseNode = mapper.createObjectNode();
		responseNode.set("response", response);

		JsonNode responseJsonNode = mapper.readTree(responseNode.toPrettyString());
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataMetroResponse bigDataMetroResponse = externalApiCallUtils.getBigDataMetro("20210601", "20210601", "1", "2");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getBody().getNumOfRows(), 2),
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getBody().getPageNo(), 1),
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getBody().getItems().getItem().size(), 2)
		);
	}

	@Test
	void getBigDataMetro_get_single_success_test() throws IOException {
		//given
		ArrayNode itemList = mapper.createArrayNode();
		itemList.add(bigDataMetroItem);

		ObjectNode item = mapper.createObjectNode();
		item.set("item", itemList);

		ObjectNode body = mapper.createObjectNode();
		body.set("items", item);
		body.put("numOfRows", 1);
		body.put("pageNo", 3);
		body.put("totalCount", 51);

		ObjectNode response = mapper.createObjectNode();
		response.set("header", header);
		response.set("body", body);

		ObjectNode responseNode = mapper.createObjectNode();
		responseNode.set("response", response);

		JsonNode responseJsonNode = mapper.readTree(responseNode.toPrettyString());
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataMetroResponse bigDataMetroResponse = externalApiCallUtils.getBigDataMetro("20210601", "20210601", "3", "1");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getBody().getNumOfRows(), 1),
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getBody().getPageNo(), 3),
			() -> Assertions.assertEquals(bigDataMetroResponse.getResponse().getBody().getItems().getItem().size(), 1)
		);
	}

	@Test
	void getBigDataLocal_get_multiple_success_test() throws IOException {
		//given
		ObjectNode body = mapper.createObjectNode();
		body.put("items", "");
		body.put("numOfRows", 3);
		body.put("pageNo", 1000);
		body.put("totalCount", 748);

		ObjectNode response = mapper.createObjectNode();
		response.set("header", header);
		response.set("body", body);

		ObjectNode responseNode = mapper.createObjectNode();
		responseNode.set("response", response);

		JsonNode responseJsonNode = mapper.readTree(responseNode.toPrettyString());
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataLocalResponse bigDataLocalResponse = externalApiCallUtils.getBigDataLocal("20210601", "20210601", "1000", "3");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getBody().getNumOfRows(), 3),
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getBody().getPageNo(), 1000),
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getBody().getItems().getItem().size(), 0)
		);
	}

	@Test
	void getBigDataLocal_get_empty_sucess_test() throws IOException {
		//given
		ObjectNode body = mapper.createObjectNode();
		body.put("items", "");
		body.put("numOfRows", 3);
		body.put("pageNo", -1);
		body.put("totalCount", 748);

		ObjectNode response = mapper.createObjectNode();
		response.set("header", header);
		response.set("body", body);

		ObjectNode responseNode = mapper.createObjectNode();
		responseNode.set("response", response);

		JsonNode responseJsonNode = mapper.readTree(responseNode.toPrettyString());
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataLocalResponse bigDataLocalResponse = externalApiCallUtils.getBigDataLocal("20210601", "20210601", "-1", "3");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getBody().getNumOfRows(), 3),
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getBody().getPageNo(), -1),
			() -> Assertions.assertEquals(bigDataLocalResponse.getResponse().getBody().getItems().getItem().size(), 0)
		);
	}
}
