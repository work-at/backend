package com.workat.common.util;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.workat.api.tour.BigDataLocalResponse;
import com.workat.api.tour.BigDataMetroResponse;

@SpringBootTest
public class ExternelApiCallUtilsTest {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ExternalApiCallUtils externalApiCallUtils;

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
