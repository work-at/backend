package com.workat.common.util;

import java.io.IOException;

import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.workat.api.tour.BigDataLocalResponse;
import com.workat.api.tour.BigDataMetroResponse;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExternalApiCallUtilsTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ExternalApiCallUtils externalApiCallUtils;

	@BeforeAll
	public static void setup(@Autowired TestRestTemplate restTemplate) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(5000);
		factory.setHttpClient(
			HttpClientBuilder.create()
				.setMaxConnTotal(5)
				.setMaxConnPerRoute(5)
				.build());
		restTemplate.getRestTemplate().setRequestFactory(factory);
	}

	@Test
	void getBigDataMetro_get_multiple_success_test() throws IOException {
		BigDataMetroResponse response = externalApiCallUtils.getBigDataMetro("20210601", "20210601", "1", "2");
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 2),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), 1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 2)
		);
	}

	@Test
	void getBigDataMetro_get_single_success_test() throws IOException {
		BigDataMetroResponse response = externalApiCallUtils.getBigDataMetro("20210601", "20210601", "3", "1");
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), 3),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 1)
		);
	}

	@Test
	void getBigDataLocal_get_multiple_success_test() throws IOException {
		BigDataLocalResponse response = externalApiCallUtils.getBigDataLocal("20210601", "20210601", "1", "3");
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 3),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), 1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 3)
		);
	}

	@Test
	void getBigDataLocal_get_empty_sucess_test() throws IOException {
		BigDataLocalResponse response = externalApiCallUtils.getBigDataLocal("20210601", "20210601", "-1", "3");
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getResponse().getHeader().getResultMsg(), "OK"),
			() -> Assertions.assertEquals(response.getResponse().getBody().getNumOfRows(), 3),
			() -> Assertions.assertEquals(response.getResponse().getBody().getPageNo(), -1),
			() -> Assertions.assertEquals(response.getResponse().getBody().getItems().getItem().size(), 0)
		);
	}
}
