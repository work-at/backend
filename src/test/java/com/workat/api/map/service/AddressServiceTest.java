package com.workat.api.map.service;

import static org.mockito.BDDMockito.*;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

	@Mock
	private LocationHttpReceiver locationHttpReceiver;

	@Mock
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	@InjectMocks
	private AddressService addressService;

	private final ObjectMapper mapper = new ObjectMapper();

	private ObjectNode response;

	@BeforeEach
	void init() {
		ObjectNode roadAddress = mapper.createObjectNode();
		roadAddress.put("address_name", "경기도 안성시 죽산면 죽산초교길 69-4");
		roadAddress.put("region_1depth_name", "경기");
		roadAddress.put("region_2depth_name", "안성시");
		roadAddress.put("region_3depth_name", "죽산면");
		roadAddress.put("road_name", "죽산초교길");
		roadAddress.put("underground_yn", "N");
		roadAddress.put("main_building_no", "69");
		roadAddress.put("sub_building_no", "4");
		roadAddress.put("building_name", "무지개아파트");
		roadAddress.put("zone_no", "17519");
		ObjectNode address = mapper.createObjectNode();
		address.put("address_name", "경기 안성시 죽산면 죽산리 343-1");
		address.put("region_1depth_name", "경기");
		address.put("region_2depth_name", "안성시");
		address.put("region_3depth_name", "죽산면 죽산리");
		address.put("mountain_yn", "N");
		address.put("main_address_no", "343");
		address.put("sub_address_no", "1");
		address.put("zip_code", "");
		ObjectNode document = mapper.createObjectNode();
		document.set("road_address", roadAddress);
		document.set("address", address);
		ArrayNode documents = mapper.createArrayNode();
		documents.add(document);
		ObjectNode meta = mapper.createObjectNode();
		meta.put("total_count", 1);
		response = mapper.createObjectNode();
		response.set("meta", meta);
		response.set("documents", documents);
	}

	@Test
	void testSaveAddress() throws JsonProcessingException {
		//given
		WorkerLocation workerLocation = WorkerLocation.of("123", "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		String responseString = response.toPrettyString();
		JsonNode responseJsonNode = mapper.readTree(responseString);
		KakaoAddressResponse kakaoResponse = mapper.treeToValue(responseJsonNode, KakaoAddressResponse.class);
		Mockito.when(locationHttpReceiver.getAddress("127.423084873712", "37.0789561558879")).thenReturn(kakaoResponse);
		Mockito.when(workerLocationRedisRepository.save(any())).thenReturn(workerLocation);

		//when
		addressService.saveAddress("123", "127.423084873712", "37.0789561558879");

		//then
		verify(workerLocationRedisRepository, times(1)).save(any());

	}
}
