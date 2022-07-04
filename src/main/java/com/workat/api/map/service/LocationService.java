package com.workat.api.map.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.map.dto.MapLocationDto;
import com.workat.api.map.dto.request.LocationRequest;
import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.LocationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationService {

	private final LocationHttpReceiver locationHttpReceiver;

	private final LocationRepository locationRepository;

	public LocationResponse getLocations(LocationCategory category, LocationRequest request) {
		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		List<Location> locations = locationRepository.findAllByCategory(category);

		if (locations.isEmpty()) {
			throw new NotFoundException("location not found exception");
		}

		List<MapLocationDto> mapLocationDtos = locations.stream()
			.map(location -> MapLocationDto.builder()
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

		return LocationResponse.of(mapLocationDtos);
	}

	public void updateLocations(LocationTriggerRequest request) {
		for (LocationCategory category : LocationCategory.values()) {
			List<KakaoLocalDataDto> locationDtos = locationHttpReceiver.updateLocations(category, request);
			List<Location> locations = locationDtos.stream()
				.distinct()
				.map(dto -> {
					Location location = locationRepository.findByPlaceId(dto.getId())
						.orElseGet(() -> parseDtoToLocation(category, dto));
					return location.update(dto);
				})
				.collect(Collectors.toList());
			locationRepository.saveAll(locations);
		}
	}

	private Location parseDtoToLocation(LocationCategory category, KakaoLocalDataDto dto) {
		return Location.builder()
			.category(category)
			.dto(dto)
			.build();
	}
}
