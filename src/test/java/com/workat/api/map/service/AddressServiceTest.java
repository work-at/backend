package com.workat.api.map.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.workat.domain.map.entity.WorkerLocation;

@ActiveProfiles("test")
@SpringBootTest
public class AddressServiceTest {

	@Autowired
	private AddressService addressService;

	@Test
	void testSaveAddress() {
		//given
		WorkerLocation saved = addressService.saveAddress("123", "127.423084873712", "37.0789561558879");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(saved.getUserId(), "123"),
			() -> Assertions.assertEquals(saved.getLatitude(), "37.0789561558879"),
			() -> Assertions.assertEquals(saved.getLongitude(), "127.423084873712"),
			() -> Assertions.assertEquals(saved.getAddress(), "경기 안성시 죽산면 죽산리")
		);

	}
}
