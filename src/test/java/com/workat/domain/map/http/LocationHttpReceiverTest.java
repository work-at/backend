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
import com.workat.api.map.dto.LocationRequest;
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
	void getLocation() {
		//given
		LocationCategory givenLocationCategory = LocationCategory.CAFE;
		LocationRequest givenLocationRequest = LocationRequest.of(1.0f, 1.0f, 1, 1);
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
		KakaoLocalResponse response = locationHttpReceiver.getLocation(givenLocationCategory, givenLocationRequest);

		//then
		assertTrue(response.getDocuments().containsAll(givenDocuments));
		assertEquals(response.getMeta(), givenMetaDto);

	}

	@Test
	void getAddress() throws IOException {
		//given
		String responseString = "{\n"
			+ "    \"meta\": {\n"
			+ "        \"total_count\": 1\n"
			+ "    },\n"
			+ "    \"documents\": [\n"
			+ "        {\n"
			+ "            \"road_address\": {\n"
			+ "                \"address_name\": \"경기도 안성시 죽산면 죽산초교길 69-4\",\n"
			+ "                \"region_1depth_name\": \"경기\",\n"
			+ "                \"region_2depth_name\": \"안성시\",\n"
			+ "                \"region_3depth_name\": \"죽산면\",\n"
			+ "                \"road_name\": \"죽산초교길\",\n"
			+ "                \"underground_yn\": \"N\",\n"
			+ "                \"main_building_no\": \"69\",\n"
			+ "                \"sub_building_no\": \"4\",\n"
			+ "                \"building_name\": \"무지개아파트\",\n"
			+ "                \"zone_no\": \"17519\"\n"
			+ "            },\n"
			+ "            \"address\": {\n"
			+ "                \"address_name\": \"경기 안성시 죽산면 죽산리 343-1\",\n"
			+ "                \"region_1depth_name\": \"경기\",\n"
			+ "                \"region_2depth_name\": \"안성시\",\n"
			+ "                \"region_3depth_name\": \"죽산면 죽산리\",\n"
			+ "                \"mountain_yn\": \"N\",\n"
			+ "                \"main_address_no\": \"343\",\n"
			+ "                \"sub_address_no\": \"1\",\n"
			+ "                \"zip_code\": \"\"\n"
			+ "            }\n"
			+ "        }\n"
			+ "    ]\n"
			+ "}";
		JsonNode responseJsonNode = mapper.readTree(responseString);
		KakaoAddressResponse kakaoResponse = mapper.treeToValue(responseJsonNode, KakaoAddressResponse.class);
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(kakaoResponse));

		//when
		KakaoAddressResponse response = locationHttpReceiver.getAddress("127.423084873712", "37.0789561558879");

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getMeta().getTotalCount(), kakaoResponse.getMeta().getTotalCount()),
			() -> Assertions.assertEquals(response.getDocuments().size(), kakaoResponse.getDocuments().size()),
			() -> Assertions.assertEquals(response.getDocuments().get(0).getAddress().getRegion1DepthName(), kakaoResponse.getDocuments().get(0).getAddress().getRegion1DepthName()),
			() -> Assertions.assertEquals(response.getDocuments().get(0).getRoadAddress().getRegion2DepthName(), kakaoResponse.getDocuments().get(0).getRoadAddress().getRegion2DepthName())
		);

	}
}
