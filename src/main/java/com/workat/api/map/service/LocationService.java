package com.workat.api.map.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.map.dto.LocationDto;
import com.workat.api.map.dto.LocationRequest;
import com.workat.api.map.dto.LocationResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.http.dto.KakaoLocalMetaDto;
import com.workat.domain.map.http.dto.KakaoLocalResponse;
import com.workat.domain.map.repository.LocationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationService {

	private final LocationHttpReceiver locationHttpReceiver;

	private final LocationRepository locationRepository;

	public LocationResponse getLocation(String category, LocationRequest locationRequest) {
		LocationCategory locationCategory = LocationCategory.of(category);

		if (locationCategory == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		List<Location> locations = getLocationFromKakao(locationCategory, locationRequest);

		List<LocationDto> locationDtos = locations.stream()
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

		return LocationResponse.of(locationDtos);
	}

	private List<Location> getLocationFromKakao(LocationCategory category, LocationRequest request) {
		KakaoLocalResponse response = locationHttpReceiver.getLocation(category, request);
		List<KakaoLocalDataDto> data = response.getDocuments();
		KakaoLocalMetaDto metaDto = response.getMeta();

		List<Location> locations = data.stream()
			.map(dto -> Location.builder()
				.category(category)
				.dto(dto)
				.build())
			.collect(Collectors.toList());

		return locationRepository.saveAll(locations);
	}
}
