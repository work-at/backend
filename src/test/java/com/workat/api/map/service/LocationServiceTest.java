package com.workat.api.map.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.review.service.ReviewService;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.area.repository.AreaRepository;
import com.workat.domain.config.MysqlContainerBaseTest;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.http.dto.KakaoLocalMetaDto;
import com.workat.domain.map.http.dto.KakaoLocalResponse;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.review.repository.CafeReviewRepository;
import com.workat.domain.review.repository.RestaurantReviewRepository;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class LocationServiceTest extends MysqlContainerBaseTest {

	private LocationService locationService;

	private LocationHttpReceiver locationHttpReceiver;

	private ReviewService reviewService;

	@Mock
	private RestTemplate restTemplate;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private CafeReviewRepository cafeReviewRepository;

	@Autowired
	private RestaurantReviewRepository restaurantReviewRepository;

	@BeforeEach
	void setUp() {
		this.locationHttpReceiver = new LocationHttpReceiver(restTemplate);
		this.reviewService = new ReviewService(cafeReviewRepository, restaurantReviewRepository, locationRepository);
		this.locationService = new LocationService(locationHttpReceiver, reviewService, locationRepository,
			areaRepository);
	}

	@Test
	void getLocations_fail_category_invalid() {
		//given

		//when

		//then
		assertThrows(BadRequestException.class,
			() -> locationService.getLocations(false, null, 1.0, 1.0, 1));
	}

	@Test
	void getLocations_fail_location_not_found_1() {
		//given

		//when

		//then
		assertThrows(NotFoundException.class,
			() -> locationService.getLocations(false, LocationCategory.CAFE, 1.0, 1.0, 1));
	}

	@Test
	void getLocations_fail_location_not_found_2() {
		//given

		//when

		//then
		assertThrows(NotFoundException.class,
			() -> locationService.getLocations(false, LocationCategory.RESTAURANT, 1.0, 1.0, 1));
	}

	// TODO: 2022/07/13 거리 관련 로직 확정전이라 exception이 무조건 발생
	// @Test
	// void getLocations_success() {
	// 	//given
	// 	ArrayList<Location> givenLocations = new ArrayList<>();
	//
	// 	int givenSize = 10;
	// 	for (int i = 0; i < givenSize; i++) {
	// 		String value = String.valueOf(i);
	// 		KakaoLocalDataDto dto = KakaoLocalDataDto.builder()
	// 			.id(value)
	// 			.phone("000-0000-0000")
	// 			.placeName(value)
	// 			.placeUrl(value)
	// 			.addressName(value)
	// 			.roadAddressName(value)
	// 			.longitude(value)
	// 			.latitude(value)
	// 			.build();
	//
	// 		Location location = Location.builder()
	// 			.category(LocationCategory.CAFE)
	// 			.dto(dto)
	// 			.build();
	//
	// 		givenLocations.add(location);
	// 	}
	// 	locationRepository.saveAll(givenLocations);
	//
	// 	//when
	// 	LocationResponse response = locationService.getLocations(false, LocationCategory.CAFE, 1.0,
	// 		1.0, 1);
	// 	List<LocationDetailDto> givenDtos = givenLocations.stream()
	// 		.map(location -> LocationDetailDto.builder()
	// 			.id(location.getId())
	// 			.category(location.getCategory())
	// 			.phone(location.getPhone())
	// 			.placeId(location.getPlaceId())
	// 			.placeUrl(location.getPlaceUrl())
	// 			.placeName(location.getPlaceName())
	// 			.longitude(location.getLongitude())
	// 			.latitude(location.getLatitude())
	// 			.build())
	// 		.collect(Collectors.toList());
	//
	// 	//then
	// 	assertEquals(givenSize, response.getData().size());
	// 	for (int i = 0; i < givenSize; i++) {
	// 		LocationDetailDto given = givenDtos.get(i);
	// 		LocationDetailDto result = (LocationDetailDto)response.getData().get(i);
	//
	// 		assertAll(
	// 			() -> assertEquals(given.getId(), result.getId()),
	// 			() -> assertEquals(given.getCategory(), result.getCategory()),
	// 			() -> assertEquals(given.getPhone(), result.getPhone()),
	// 			() -> assertEquals(given.getPlaceId(), result.getPlaceId()),
	// 			() -> assertEquals(given.getPlaceName(), result.getPlaceName()),
	// 			() -> assertEquals(given.getPlaceUrl(), result.getPlaceUrl()),
	// 			() -> assertEquals(given.getLongitude(), result.getLongitude()),
	// 			() -> assertEquals(given.getLatitude(), result.getLatitude())
	// 		);
	// 	}
	// }

	@Test
	void getLocationById_fail_not_found() {
		//given

		//when

		//then
		assertThrows(NotFoundException.class, () -> locationService.getLocationById(LocationCategory.CAFE, 1L, 1L));
	}

	@Test
	void getLocationById_success() {
		//given
		String value = "1";
		KakaoLocalDataDto dto = KakaoLocalDataDto.builder()
			.id(value)
			.phone("000-0000-0000")
			.placeName(value)
			.placeUrl(value)
			.addressName(value)
			.roadAddressName(value)
			.x(value)
			.y(value)
			.build();

		Location given = Location.builder()
			.category(LocationCategory.CAFE)
			.dto(dto)
			.build();

		Users user = Users.of(OauthType.KAKAO, 1L);
		UserProfile userProfile = UserProfile.builder()
			.user(user)
			.nickname("holden")
			.position(DepartmentType.ACCOUNTANT)
			.workingYear(DurationType.JUNIOR)
			.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
			.build();

		long locationid = locationRepository.save(given).getId();

		//when
		LocationDetailDto result = locationService.getLocationById(LocationCategory.CAFE, locationid, 1L)
			.getLocationDetail();

		//then
		LocationDetailDto givenDto = LocationDetailDto.builder()
			.id(given.getId())
			.category(given.getCategory())
			.phone(given.getPhone())
			.placeId(given.getPlaceId())
			.placeUrl(given.getPlaceUrl())
			.placeName(given.getPlaceName())
			.longitude(given.getLongitude())
			.latitude(given.getLatitude())
			.build();

		assertAll(
			() -> assertEquals(givenDto.getId(), result.getId()),
			() -> assertEquals(givenDto.getCategory(), result.getCategory()),
			() -> assertEquals(givenDto.getPhone(), result.getPhone()),
			() -> assertEquals(givenDto.getPlaceId(), result.getPlaceId()),
			() -> assertEquals(givenDto.getPlaceName(), result.getPlaceName()),
			() -> assertEquals(givenDto.getPlaceUrl(), result.getPlaceUrl()),
			() -> assertEquals(givenDto.getLongitude(), result.getLongitude()),
			() -> assertEquals(givenDto.getLatitude(), result.getLatitude())
		);
	}

	@Test
	void updateLocations_success() {
		//given
		LocationTriggerRequest request = LocationTriggerRequest.of(1.0, 1.0, 1);

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
		KakaoLocalResponse response = KakaoLocalResponse.builder()
			.documents(givenDocuments)
			.meta(givenMetaDto)
			.build();

		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.willReturn(ResponseEntity.ok(response));

		//when
		assertEquals(0, locationRepository.findAll().size());
		locationService.updateLocations();

		//then
		assertEquals(10, locationRepository.findAll().size());
	}
}
