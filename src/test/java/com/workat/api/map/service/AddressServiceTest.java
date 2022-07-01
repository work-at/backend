package com.workat.api.map.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Test
	void testSaveAddress() throws JsonProcessingException {
		//given
		WorkerLocation workerLocation = WorkerLocation.of("123", "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
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
		Mockito.when(locationHttpReceiver.getAddress("127.423084873712", "37.0789561558879")).thenReturn(kakaoResponse);
		Mockito.when(workerLocationRedisRepository.save(any())).thenReturn(workerLocation);

		//when
		addressService.saveAddress("123", "127.423084873712", "37.0789561558879");

		//then
		verify(workerLocationRedisRepository, times(1)).save(any());

	}
}
