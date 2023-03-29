package com.workat.api.map.service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.map.dto.LocationBriefDto;
import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.map.dto.LocationDto;
import com.workat.api.map.dto.LocationPinDto;
import com.workat.api.map.dto.response.LocationDetailResponse;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.review.dto.ReviewWithUserDto;
import com.workat.api.review.service.ReviewService;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.DistanceUtils;
import com.workat.common.util.FileReadUtils;
import com.workat.domain.area.entity.Area;
import com.workat.domain.area.repository.AreaRepository;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.map.vo.MapPoint;
import com.workat.domain.map.vo.MapRangeInfo;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LocationService {

	private static final int TOP_REVIEW_LENGTH = 3;

	private final LocationHttpReceiver locationHttpReceiver;

	private final ReviewService reviewService;

	private final LocationRepository locationRepository;

	private final AreaRepository areaRepository;

	private final LocationImageGenerator imageGenerator;

	@Transactional(readOnly = true)
	public LocationResponse<? extends LocationDto> getLocationsWithPin(LocationType category, double longitude,
		double latitude, int radius) {

		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		MapRangeInfo mapRangeInfo = DistanceUtils.getLocationPoint(longitude, latitude, radius);
		List<Location> locations = locationRepository.findAllByRadius(category, mapRangeInfo);

		if (locations.isEmpty()) {
			return LocationResponse.of(Collections.emptyList());
		}

		List<LocationPinDto> locationPinDtos = locations.stream()
			.map(location -> LocationPinDto.of(location.getId(), location.getPlaceId(), location.getLongitude(),
				location.getLatitude()))
			.collect(Collectors.toList());

		return LocationResponse.of(locationPinDtos);
	}

	@Transactional(readOnly = true)
	public LocationResponse<? extends LocationDto> getLocationBriefs(String baseUrl,
		LocationType category, double longitude, double latitude, int radius) {

		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		MapRangeInfo mapRangeInfo = DistanceUtils.getLocationPoint(longitude, latitude, radius);

		List<Location> locations = locationRepository.findAllByRadius(category, mapRangeInfo);

		if (locations.isEmpty()) {
			return LocationResponse.of(Collections.emptyList());
		}

		List<LocationBriefDto> locationBriefs = locations.stream()
			.map(location -> {
				long locationId = location.getId();
				List<TagCountDto> locationReviews = reviewService.getLocationReviews(locationId, category);

				int reviewCount = reviewService.countDistinctUserByLocationId(locationId, category);

				List<TagDto> topReviews = locationReviews.stream()
					.limit(TOP_REVIEW_LENGTH)
					.map(TagCountDto::getTag)
					.collect(Collectors.toList());

				return LocationBriefDto.from(location, reviewCount, topReviews);
			})
			.collect(Collectors.toList());

		return LocationResponse.of(locationBriefs);
	}

	public LocationDetailResponse getLocationById(String baseUrl, LocationType category, long locationId,
		long userId) {
		if (category == null) {
			throw new BadRequestException("category must be food or cafe");
		}

		// TODO: 2022/07/06  추후 Cafe와 Restaurant 분리 예정이라 그때 코드 수정
		Location location = locationRepository.findById(locationId).orElseThrow(() -> {
			throw new NotFoundException("location is not found");
		});

		LocationDetailDto locationDetailDto = LocationDetailDto.from(location,
			baseUrl + "/uploaded" + location.getFullImageUrl());

		final ReviewWithUserDto locationLocationReviewDto = reviewService.getLocationReviewsWithUser(
			userId,
			locationId,
			category
		);

		return LocationDetailResponse.of(
			locationDetailDto,
			locationLocationReviewDto
		);
	}

	@Transactional
	public long updateLocations() {
		createAreaFromCsv();

		long startLocationCount = locationRepository.findAll().size();

		for (LocationType category : LocationType.values()) {
			List<Area> areas = areaRepository.findAll();
			for (Area area : areas) {
				ArrayList<Location> result = new ArrayList<>();
				for (int x = -3; x <= 3; x++) {
					for (int y = -3; y <= 3; y++) {
						List<KakaoLocalDataDto> locationDtos = locationHttpReceiver.updateLocations(category,
							MapPoint.of(area.getLongitude() + (x / 100.0), area.getLatitude() + (y / 100.0)), 5000);

						for (KakaoLocalDataDto locationDto : locationDtos) {
							locationDto.updateImages(imageGenerator.generateImageUrl(locationDto.getCategoryName()));
						}

						List<Location> locations = locationDtos.stream()
							.filter(distinctByKey(KakaoLocalDataDto::getId))
							.map(dto -> {
								Location location = locationRepository.findByPlaceId(dto.getId())
									.orElseGet(() -> Location.of(category, dto));
								return location.update(dto);
							})
							.collect(Collectors.toList());
						result.addAll(locations);
					}
				}

				locationRepository.saveAll(result.stream()
					.filter(distinctByKey(Location::getPlaceId))
					.collect(Collectors.toList()));
			}
		}

		return locationRepository.findAll().size() - startLocationCount;
	}

	private void createAreaFromCsv() {
		areaRepository.deleteAll();
		// readSeoulSubwayCsv();
		readEtcCsv();
	}

	private void readSeoulSubwayCsv() {
		//호선, 역명, 주소, lat, long
		String seoulSubwayCsvPath = "/csv/seoul_subway_3.csv";
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

	private void readEtcCsv() {

		String seoulSubwayCsvPath = "/csv/etc_5.csv";
		URL url = getClass().getResource(seoulSubwayCsvPath);

		if (url == null || url.getPath() == null) {
			throw new NotFoundException(seoulSubwayCsvPath + " not exist");
		}
		File file = new File(url.getPath());

		List<Area> result = FileReadUtils.readCSV(file).stream()
			.map(line -> {
				String name = line.get(0);
				String latitude = line.get(1);
				String longitude = line.get(2);
				return Area.of(name, "", Double.parseDouble(longitude),
					Double.parseDouble(latitude));
			})
			.collect(Collectors.toList());

		areaRepository.saveAll(result);
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
