package com.workat.domain.map.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.workat.api.map.dto.LocationRequest;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.http.dto.KakaoLocalMetaDto;
import com.workat.domain.map.http.dto.KakaoLocalResponse;

@ExtendWith(MockitoExtension.class)
class LocationHttpReceiverTest {

	private LocationHttpReceiver locationHttpReceiver;

	@Mock
	private RestTemplate restTemplate;

	private String key = "";

	@BeforeEach
	void init() {
		this.locationHttpReceiver = new LocationHttpReceiver(key, restTemplate);
	}

	@Test
	void getLocation() {
		//given
		LocationCategory givenLocationCategory = LocationCategory.CAFE;
		LocationRequest givenLocationRequest = LocationRequest.builder()
			.x(1.0f)
			.y(1.0f)
			.radius(1)
			.page(1)
			.build();

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

		KakaoLocalMetaDto givenMetaDto = KakaoLocalMetaDto.builder()
			.isEnd(true)
			.pageableCount(1)
			.totalCount(1)
			.build();

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
}
