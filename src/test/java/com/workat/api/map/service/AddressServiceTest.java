package com.workat.api.map.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.workat.api.map.dto.request.UserAddressRequest;
import com.workat.api.map.dto.response.UserAddressResponse;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.MultipleDatasourceBaseTest;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.repository.worker.WorkerLocationRedisRepository;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest extends MultipleDatasourceBaseTest {

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	@Mock
	private LocationHttpReceiver locationHttpReceiver;

	private AddressService addressService;

	private final ObjectMapper mapper = new ObjectMapper();

	private ObjectNode response;
	private Users user;
	private WorkerLocation workerLocation, workerLocation1, workerLocation2;
	KakaoAddressResponse kakaoResponse;

	@BeforeAll
	void init() throws JsonProcessingException {
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
		String responseString = response.toPrettyString();
		JsonNode responseJsonNode = mapper.readTree(responseString);
		kakaoResponse = mapper.treeToValue(responseJsonNode, KakaoAddressResponse.class);
		Mockito.when(locationHttpReceiver.getAddress("127.423084873712", "37.0789561558879")).thenReturn(kakaoResponse);

		this.addressService = new AddressService(locationHttpReceiver, workerLocationRedisRepository);

		user = Users.of(OauthType.KAKAO, 12345L);
		UserProfile userProfile = UserProfile.builder()
			.nickname("nickname")
			.position(DepartmentType.ACCOUNTANT)
			.workingYear(DurationType.JUNIOR)
			.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
			.build();
		user.setUserProfile(userProfile);
		userProfileRepository.save(userProfile);
		userRepository.save(user);

		workerLocation = WorkerLocation.of(user.getId(), "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		workerLocation1 = WorkerLocation.of(124L, "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		workerLocation2 = WorkerLocation.of(125L, "127.51", "37.078", "경기 안성시 일죽면 산북리");
		workerLocationRedisRepository.save(workerLocation);
		workerLocationRedisRepository.save(workerLocation1);
		workerLocationRedisRepository.save(workerLocation2);
	}

	@AfterAll
	void teardown() {
		userRepository.deleteAll();
		userProfileRepository.deleteAll();
		workerLocationRedisRepository.deleteAll();
	}

	@Test
	void countWorkerByLocationNear() {
		//given

		//when
		UserAddressResponse response = addressService.saveUserAddress(user, UserAddressRequest.of("127.423084873712", "37.0789561558879"));

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getAddress(), "경기 안성시 죽산면 죽산리")
		);
	}
}
