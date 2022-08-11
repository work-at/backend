package com.workat.api.map.service;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.map.dto.LocationBriefDto;
import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.map.dto.LocationDto;
import com.workat.api.map.dto.LocationPinDto;
import com.workat.api.map.dto.response.LocationDetailResponse;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewTypeDto;
import com.workat.api.review.dto.ReviewWithUserDto;
import com.workat.api.review.service.ReviewService;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.DistanceUtils;
import com.workat.common.util.FileReadUtils;
import com.workat.domain.area.entity.Area;
import com.workat.domain.area.repository.AreaRepository;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.map.vo.MapPoint;
import com.workat.domain.map.vo.MapRangeInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationService {

	private static final int TOP_REVIEW_LENGTH = 3;

	private final LocationHttpReceiver locationHttpReceiver;

	private final ReviewService reviewService;

	private final LocationRepository locationRepository;

	private final AreaRepository areaRepository;

	// TODO: 2022/07/31 pin 과 brief 를 분리해보기
	public LocationResponse<? extends LocationDto> getLocations(boolean isPin, LocationCategory category,
		double longitude, double latitude,
		int radius) {

		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		MapRangeInfo mapRangeInfo = DistanceUtils.getLocationPoint(longitude, latitude, radius);
		List<Location> locations = locationRepository.findAllByRadius(mapRangeInfo);
		log.info("findAllByRadius : " + locations.size());

		if (locations.isEmpty()) {
			throw new NotFoundException("location not found exception");
		}

		if (isPin) {
			List<LocationPinDto> locationPinDtos = locations.stream()
				.map(location -> LocationPinDto.of(location.getId(), location.getPlaceId(), location.getLongitude(),
					location.getLatitude()))
				.collect(Collectors.toList());

			return LocationResponse.of(locationPinDtos);
		}

		List<LocationBriefDto> locationBriefs = locations.stream()
			.map(location -> {
				long locationId = location.getId();
				List<ReviewDto> locationReviews = reviewService.getLocationReviews(locationId, category);

				int reviewCount = locationReviews.size();
				List<ReviewTypeDto> topReviews = locationReviews.stream()
					.limit(TOP_REVIEW_LENGTH)
					.map(ReviewDto::getReviewType)
					.collect(Collectors.toList());

				return LocationBriefDto.builder()
					.id(locationId)
					.placeId(location.getPlaceId())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude())
					.category(location.getCategory())
					.placeName(location.getPlaceName())
					.roadAddressName(location.getRoadAddressName())
					// .thumbnailImageUrl(location.getThumbnailImageUrl()) // TODO: 파일 서버 구축되면 링크로 내려주기
					.reviewCount(reviewCount)
					.topReviews(topReviews)
					.build();
			})
			.collect(Collectors.toList());

		return LocationResponse.of(locationBriefs);
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
			.placeId(location.getPlaceId())
			.longitude(location.getLongitude())
			.latitude(location.getLatitude())
			.category(location.getCategory())
			.placeName(location.getPlaceName())
			.roadAddressName(location.getRoadAddressName())
			.phone(location.getPhone())
			.placeUrl(location.getPlaceUrl())
			.build();

		final ReviewWithUserDto locationLocationReviewDto = reviewService.getLocationReviewsWithUser(
			locationId,
			category,
			userId);

		return LocationDetailResponse.of(
			locationDetailDto,
			locationLocationReviewDto
		);
	}

	@Transactional
	public void updateLocations() {
		createAreaFromCsv();

		for (LocationCategory category : LocationCategory.values()) {
			List<Area> areas = areaRepository.findAll();
			for (Area area : areas) {
				log.info(area.getName() + " batch start");
				List<KakaoLocalDataDto> locationDtos = locationHttpReceiver.updateLocations(category,
					MapPoint.of(area.getLongitude(), area.getLatitude()), 5000);
				List<Location> locations = locationDtos.stream()
					.distinct()
					.map(dto -> {
						Location location = locationRepository.findByPlaceId(dto.getId())
							.orElseGet(() -> Location.of(category, dto));
						return location.update(dto);
					})
					.collect(Collectors.toList());
				locationRepository.saveAll(locations);
			}
		}
	}

	private void createAreaFromCsv() {
		areaRepository.deleteAll();
		readSeoulSubwayCsv();
	}

	private void readSeoulSubwayCsv() {
		//호선, 역명, 주소, lat, long
		String seoulSubwayCsvPath = "/csv/seoul_subway_1.csv";
		URL url = getClass().getResource(seoulSubwayCsvPath);

		if (url == null || url.getPath() == null) {
			throw new NotFoundException(seoulSubwayCsvPath + " not exist");
		}
		File file = new File(url.getPath());

		List<Area> result = FileReadUtils.readCSV(file).stream()
			.map(line -> {
				String stationName = line.get(1) + "역";
				String stationAddress = line.get(2);
				String latitude = line.get(3);
				String longitude = line.get(4);
				return Area.of(stationName, stationAddress, Double.parseDouble(longitude),
					Double.parseDouble(latitude));
			})
			.collect(Collectors.toList());

		areaRepository.saveAll(result);
	}
}
