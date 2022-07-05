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

import com.workat.api.map.dto.LocationDto;
import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.config.MysqlContainerBaseTest;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.http.dto.KakaoLocalMetaDto;
import com.workat.domain.map.http.dto.KakaoLocalResponse;
import com.workat.domain.map.repository.LocationRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class LocationServiceTest extends MysqlContainerBaseTest {

	private LocationService locationService;

	private LocationHttpReceiver locationHttpReceiver;

	@Mock
	private RestTemplate restTemplate;

	@Autowired
	private LocationRepository locationRepository;

	@BeforeEach
	void setUp() {
		this.locationHttpReceiver = new LocationHttpReceiver(restTemplate);
		this.locationService = new LocationService(locationHttpReceiver, locationRepository);
	}

	@Test
	void getLocations_fail_category_invalid() {
		//given

		//when

		//then
		assertThrows(BadRequestException.class,
			() -> locationService.getLocations(null, 1.0, 1.0, 1, 1));
	}

	@Test
	void getLocations_fail_location_not_found_1() {
		//given

		//when

		//then
		assertThrows(NotFoundException.class,
			() -> locationService.getLocations(LocationCategory.CAFE, 1.0, 1.0, 1, 1));
	}

	@Test
	void getLocations_fail_location_not_found_2() {
		//given

		//when

		//then
		assertThrows(NotFoundException.class,
			() -> locationService.getLocations(LocationCategory.RESTAURANT, 1.0, 1.0, 1, 1));
	}

	@Test
	void getLocations_success() {
		//given
		ArrayList<Location> givenLocations = new ArrayList<>();

		int givenSize = 10;
		for (int i = 0; i < givenSize; i++) {
			String value = String.valueOf(i);
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

			Location location = Location.builder()
				.category(LocationCategory.CAFE)
				.dto(dto)
				.build();

			givenLocations.add(location);
		}
		locationRepository.saveAll(givenLocations);

		//when
		LocationResponse response = locationService.getLocations(LocationCategory.CAFE, 1.0, 1.0, 1, 1);
		List<LocationDto> givenDtos = givenLocations.stream()
			.map(location -> LocationDto.builder()
				.id(location.getId())
				.category(location.getCategory())
				.phone(location.getPhone())
				.placeId(location.getPlaceId())
				.placeUrl(location.getPlaceUrl())
				.placeName(location.getPlaceName())
				.x(location.getX())
				.y(location.getY())
				.build())
			.collect(Collectors.toList());

		//then
		assertEquals(givenSize, response.getLocations().size());
		for (int i = 0; i < givenSize; i++) {
			LocationDto given = givenDtos.get(i);
			LocationDto result = response.getLocations().get(i);

			assertAll(
				() -> assertEquals(given.getId(), result.getId()),
				() -> assertEquals(given.getCategory(), result.getCategory()),
				() -> assertEquals(given.getPhone(), result.getPhone()),
				() -> assertEquals(given.getPlaceId(), result.getPlaceId()),
				() -> assertEquals(given.getPlaceName(), result.getPlaceName()),
				() -> assertEquals(given.getPlaceUrl(), result.getPlaceUrl()),
				() -> assertEquals(given.getX(), result.getX()),
				() -> assertEquals(given.getY(), result.getY())
			);
		}
	}

	@Test
	void getLocationById_fail_not_found() {
		//given

		//when

		//then
		assertThrows(NotFoundException.class, () -> locationService.getLocationById(LocationCategory.CAFE, 1L));
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

		long locationid = locationRepository.save(given).getId();

		//when
		LocationDto result = locationService.getLocationById(LocationCategory.CAFE,locationid);

		//then
		LocationDto givenDto = LocationDto.builder()
			.id(given.getId())
			.category(given.getCategory())
			.phone(given.getPhone())
			.placeId(given.getPlaceId())
			.placeUrl(given.getPlaceUrl())
			.placeName(given.getPlaceName())
			.x(given.getX())
			.y(given.getY())
			.build();

		assertAll(
			() -> assertEquals(givenDto.getId(), result.getId()),
			() -> assertEquals(givenDto.getCategory(), result.getCategory()),
			() -> assertEquals(givenDto.getPhone(), result.getPhone()),
			() -> assertEquals(givenDto.getPlaceId(), result.getPlaceId()),
			() -> assertEquals(givenDto.getPlaceName(), result.getPlaceName()),
			() -> assertEquals(givenDto.getPlaceUrl(), result.getPlaceUrl()),
			() -> assertEquals(givenDto.getX(), result.getX()),
			() -> assertEquals(givenDto.getY(), result.getY())
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
		locationService.updateLocations(request);

		//then
		assertEquals(10, locationRepository.findAll().size());
	}
}
