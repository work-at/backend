package com.workat.api.map.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.map.dto.LocationPinDto;
import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.api.map.dto.response.LocationDetailResponse;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.review.dto.ReviewsDto;
import com.workat.api.review.service.ReviewService;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.map.vo.MapPoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationService {

	private final LocationHttpReceiver locationHttpReceiver;

	private final LocationRepository locationRepository;

	private final ReviewService reviewService;

	public LocationResponse getLocations(boolean isPin, LocationCategory category, double longitude, double latitude,
		int radius) {

		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		// TODO: 2022/07/13 거리 로직 확정 후 적용 예정
		MapPoint minPoint = MapPoint.of(longitude, latitude);
		MapPoint maxPoint = MapPoint.of(longitude, latitude);

		List<Location> locations = locationRepository.findAllByRadius(minPoint, maxPoint);

		if (locations.isEmpty()) {
			throw new NotFoundException("location not found exception");
		}

		if (isPin) {
			List<LocationPinDto> locationPinDtos = locations.stream()
				.map(location -> LocationPinDto.of(location.getId(), location.getPlaceId(), location.getLongitude(),
					location.getLatitude()))
				.collect(Collectors.toList());

			return LocationResponse.of(locationPinDtos);
		} else {
			List<LocationDetailDto> locationDetailDtos = locations.stream()
				.map(location -> LocationDetailDto.builder()
					.id(location.getId())
					.category(location.getCategory())
					.phone(location.getPhone())
					.placeId(location.getPlaceId())
					.placeUrl(location.getPlaceUrl())
					.placeName(location.getPlaceName())
					.longitude(location.getLongitude())
					.latitude(location.getLatitude())
					.build())
				.collect(Collectors.toList());

			return LocationResponse.of(locationDetailDtos);
		}
	}

	public LocationDetailResponse getLocationById(LocationCategory category, long locationId, long userId) {
		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		// TODO: 2022/07/06  추후 Cafe와 Restaurant 분리 예정이라 그때 코드 수정
		Location location = locationRepository.findById(locationId).orElseThrow(() -> {
			throw new NotFoundException("location is not found");
		});

		final LocationDetailDto locationDetailDto = LocationDetailDto.builder()
			.id(location.getId())
			.category(location.getCategory())
			.phone(location.getPhone())
			.placeId(location.getPlaceId())
			.placeUrl(location.getPlaceUrl())
			.placeName(location.getPlaceName())
			.longitude(location.getLongitude())
			.latitude(location.getLatitude())
			.build();

		final ReviewsDto locationReviewsDto = reviewService.getLocationReviews(locationId, userId);

		return LocationDetailResponse.of(
			locationDetailDto,
			locationReviewsDto
		);
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

	public void out() {
		//시작지점 longitude(경도) : 126.734086 / latitude(위도) : 37.413294
		// 끝지점 longitude(경도) : 127.269311 / latitude(위도) : 37.715133
		//1. 시작 지점을 정한다.
		//2. 그 곳을 기준으로 반경 250미터의 중심의 좌표를 구한다.
		//3. 그 반경을 탐색하고 local db에 저장한다.
		//4. 끝 지점까지 반복한다.

	}

	private void secondLogic() {

	}

	private Location parseDtoToLocation(LocationCategory category, KakaoLocalDataDto dto) {
		return Location.builder()
			.category(category)
			.dto(dto)
			.build();
	}
}
