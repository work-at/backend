package com.workat.domain.map.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.http.dto.KakaoLocalMetaDto;
import com.workat.domain.map.http.dto.KakaoLocalResponse;

@ExtendWith(MockitoExtension.class)
class LocationHttpReceiverTest {

	private LocationHttpReceiver locationHttpReceiver;

	@Mock
	private RestTemplate restTemplate;

	private String key = "7052acd04b3385c80fac9bb40d8b5a32";

	private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void init() {
		this.locationHttpReceiver = new LocationHttpReceiver(restTemplate);
	}

	@Test
	void updateLocation() {
		//given
		LocationCategory givenLocationCategory = LocationCategory.CAFE;
		LocationTriggerRequest givenLocationTriggerRequest = LocationTriggerRequest.of(1.0f, 1.0f, 1);
		List<KakaoLocalDataDto> givenDocuments = IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
			.boxed()
			.map(index -> {
				String i = String.valueOf(index);
				return KakaoLocalDataDto.builder()
					.id(i)
					.x(i)
					.y(i)
					.phone(i)
					.placeName(i)
					.placeUrl(i)
					.addressName(i)
					.roadAddressName(i)
					.build();
			})
			.collect(Collectors.toList());

		KakaoLocalMetaDto givenMetaDto = KakaoLocalMetaDto.of(true, 1, 1);

		KakaoLocalResponse givenResponse = KakaoLocalResponse.builder()
			.documents(givenDocuments)
			.meta(givenMetaDto)
			.build();

		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(givenResponse));

		//when
		List<KakaoLocalDataDto> response = locationHttpReceiver.updateLocations(givenLocationCategory,
			givenLocationTriggerRequest);

		//then
		assertEquals(givenResponse.getDocuments().size(), response.size());
		for (int i = 0; i < response.size(); i++) {
			KakaoLocalDataDto given = givenDocuments.get(i);
			KakaoLocalDataDto result = response.get(i);

			assertAll(
				() -> assertEquals(given.getId(), result.getId()),
				() -> assertEquals(given.getPlaceName(), result.getPlaceName()),
				() -> assertEquals(given.getPlaceUrl(), result.getPlaceUrl()),
				() -> assertEquals(given.getX(), result.getX()),
				() -> assertEquals(given.getY(), result.getY()),
				() -> assertEquals(given.getPhone(), result.getPhone()),
				() -> assertEquals(given.getAddressName(), result.getAddressName()),
				() -> assertEquals(given.getRoadAddressName(), result.getRoadAddressName())
			);
		}
	}

	@Test
	void getAddress() throws IOException {
		//given
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

		ObjectNode response = mapper.createObjectNode();
		response.set("meta", meta);
		response.set("documents", documents);

		String responseString = response.toPrettyString();
		JsonNode responseJsonNode = mapper.readTree(responseString);
		KakaoAddressResponse kakaoResponse = mapper.treeToValue(responseJsonNode, KakaoAddressResponse.class);

		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(kakaoResponse));

		//when
		KakaoAddressResponse kakaoAddressResponse = locationHttpReceiver.getAddress("127.423084873712",
			"37.0789561558879");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(kakaoAddressResponse.getMeta().getTotalCount(),
				kakaoResponse.getMeta().getTotalCount()),
			() -> Assertions.assertEquals(kakaoAddressResponse.getDocuments().size(),
				kakaoResponse.getDocuments().size()),
			() -> Assertions.assertEquals(kakaoAddressResponse.getDocuments().get(0).getAddress().getRegion1DepthName(),
				kakaoResponse.getDocuments().get(0).getAddress().getRegion1DepthName()),
			() -> Assertions.assertEquals(
				kakaoAddressResponse.getDocuments().get(0).getRoadAddress().getRegion2DepthName(),
				kakaoResponse.getDocuments().get(0).getRoadAddress().getRegion2DepthName())
		);

	}
}
