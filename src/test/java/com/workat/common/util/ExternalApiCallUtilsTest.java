package com.workat.common.util;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import com.workat.api.tour.BigDataLocalResponse;
import com.workat.api.tour.BigDataMetroResponse;

@ExtendWith(MockitoExtension.class)
public class ExternalApiCallUtilsTest {

	@Mock
	private RestTemplate restTemplate;

	private ExternalApiCallUtils externalApiCallUtils;

	private final ObjectMapper mapper = new ObjectMapper();

	private final String token = "2scFfN6uVjhuRcXrj1DgGjslYn0wYJc7kvCOHHwOI/0kr1sjf2OqLLjq5SvCt3jKyr4JPHr/K3QGuXhvv6HYlQ==";

	@BeforeEach
	public void setUp() {
		externalApiCallUtils = new ExternalApiCallUtils(restTemplate);
		Field field = ReflectionUtils.findField(externalApiCallUtils.getClass(), "tourApiBigDataToken");
		ReflectionUtils.makeAccessible(field);
		ReflectionUtils.setField(field, externalApiCallUtils, token);
	}

	@Test
	void getBigDataMetro_get_multiple_success_test() throws IOException {
		//given
		String responseString = "{\n"
			+ "    \"response\": {\n"
			+ "        \"header\": {\n"
			+ "            \"resultCode\": \"0000\",\n"
			+ "            \"resultMsg\": \"OK\"\n"
			+ "        },\n"
			+ "        \"body\": {\n"
			+ "            \"items\": {\n"
			+ "                \"item\": [\n"
			+ "                    {\n"
			+ "                        \"areaCode\": 11,\n"
			+ "                        \"areaNm\": \"서울특별시\",\n"
			+ "                        \"baseYmd\": 20210625,\n"
			+ "                        \"daywkDivCd\": 5,\n"
			+ "                        \"daywkDivNm\": \"금요일\",\n"
			+ "                        \"touDivCd\": 1,\n"
			+ "                        \"touDivNm\": \"현지인(a)\",\n"
			+ "                        \"touNum\": 4976384\n"
			+ "                    },\n"
			+ "                    {\n"
			+ "                        \"areaCode\": 11,\n"
			+ "                        \"areaNm\": \"서울특별시\",\n"
			+ "                        \"baseYmd\": 20210625,\n"
			+ "                        \"daywkDivCd\": 5,\n"
			+ "                        \"daywkDivNm\": \"금요일\",\n"
			+ "                        \"touDivCd\": 2,\n"
			+ "                        \"touDivNm\": \"외지인(b)\",\n"
			+ "                        \"touNum\": 1575196\n"
			+ "                    }\n"
			+ "                ]\n"
			+ "            },\n"
			+ "            \"numOfRows\": 2,\n"
			+ "            \"pageNo\": 1,\n"
			+ "            \"totalCount\": 51\n"
			+ "        }\n"
			+ "    }\n"
			+ "}";
		JsonNode responseJsonNode = mapper.readTree(responseString);
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataMetroResponse response = externalApiCallUtils.getBigDataMetro("20210601", "20210601", "1", "2");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 2),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), 1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 2)
		);
	}

	@Test
	void getBigDataMetro_get_single_success_test() throws IOException {
		//given
		String responseString = "{\n"
			+ "    \"response\": {\n"
			+ "        \"header\": {\n"
			+ "            \"resultCode\": \"0000\",\n"
			+ "            \"resultMsg\": \"OK\"\n"
			+ "        },\n"
			+ "        \"body\": {\n"
			+ "            \"items\": {\n"
			+ "                \"item\": {\n"
			+ "                    \"areaCode\": 11,\n"
			+ "                    \"areaNm\": \"서울특별시\",\n"
			+ "                    \"baseYmd\": 20210625,\n"
			+ "                    \"daywkDivCd\": 5,\n"
			+ "                    \"daywkDivNm\": \"금요일\",\n"
			+ "                    \"touDivCd\": 3,\n"
			+ "                    \"touDivNm\": \"외국인(c)\",\n"
			+ "                    \"touNum\": 4834.352182283618\n"
			+ "                }\n"
			+ "            },\n"
			+ "            \"numOfRows\": 1,\n"
			+ "            \"pageNo\": 3,\n"
			+ "            \"totalCount\": 51\n"
			+ "        }\n"
			+ "    }\n"
			+ "}";
		JsonNode responseJsonNode = mapper.readTree(responseString);
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataMetroResponse response = externalApiCallUtils.getBigDataMetro("20210601", "20210601", "3", "1");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), 3),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 1)
		);
	}

	@Test
	void getBigDataLocal_get_multiple_success_test() throws IOException {
		//given
		String responseString = "{\n"
			+ "    \"response\": {\n"
			+ "        \"header\": {\n"
			+ "            \"resultCode\": \"0000\",\n"
			+ "            \"resultMsg\": \"OK\"\n"
			+ "        },\n"
			+ "        \"body\": {\n"
			+ "            \"items\": \"\",\n"
			+ "            \"numOfRows\": 3,\n"
			+ "            \"pageNo\": 1000,\n"
			+ "            \"totalCount\": 748\n"
			+ "        }\n"
			+ "    }\n"
			+ "}";
		JsonNode responseJsonNode = mapper.readTree(responseString);
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataLocalResponse response = externalApiCallUtils.getBigDataLocal("20210601", "20210601", "1000", "3");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 3),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), 1000),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 0)
		);
	}

	@Test
	void getBigDataLocal_get_empty_sucess_test() throws IOException {
		//given
		String responseString = "{\n"
			+ "    \"response\": {\n"
			+ "        \"header\": {\n"
			+ "            \"resultCode\": \"0000\",\n"
			+ "            \"resultMsg\": \"OK\"\n"
			+ "        },\n"
			+ "        \"body\": {\n"
			+ "            \"items\": \"\",\n"
			+ "            \"numOfRows\": 3,\n"
			+ "            \"pageNo\": -1,\n"
			+ "            \"totalCount\": 748\n"
			+ "        }\n"
			+ "    }\n"
			+ "}";
		JsonNode responseJsonNode = mapper.readTree(responseString);
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(responseJsonNode));

		//when
		BigDataLocalResponse response = externalApiCallUtils.getBigDataLocal("20210601", "20210601", "-1", "3");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 3),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), -1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 0)
		);
	}
}
