package com.workat.api.map.controller;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.response.LocationDetailResponse;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.map.service.LocationService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.user.entity.Users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Map Location Api")
@Validated
@RequiredArgsConstructor
@RestController
public class LocationController {

	private static final String DEFAULT_RADIUS = "2000";

	private final LocationService locationService;

	@ApiOperation("현재 위치 기준으로 반경(radius) 내 카페 정보를 가져오기")
	@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "radius", value = "반경", dataType = "Integer", example = "0", defaultValue = DEFAULT_RADIUS)
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success")
	})
	@GetMapping("/api/v1/map/cafes")
	public ResponseEntity<LocationResponse> getCafes(@UserValidation Users user,
		@NotNull @RequestParam double longitude,
		@NotNull @RequestParam double latitude,
		@RequestParam(required = false, defaultValue = DEFAULT_RADIUS) int radius) {
		LocationResponse response = locationService.getLocations(false, LocationCategory.CAFE, longitude, latitude,
			radius);
		return ResponseEntity.ok(response);
	}

	@ApiOperation("현재 위치 기준으로 반경(radius) 내 카페의 핀 정보를 가져오기")
	@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "radius", value = "반경", dataType = "Integer", example = "0", defaultValue = DEFAULT_RADIUS)
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success")
	})
	@GetMapping("/api/v1/map/cafes/pin")
	public ResponseEntity<LocationResponse> getCafesPin(@UserValidation Users user,
		@NotNull @RequestParam double longitude,
		@NotNull @RequestParam double latitude,
		@RequestParam(required = false, defaultValue = DEFAULT_RADIUS) int radius) {
		LocationResponse response =
			locationService.getLocations(true, LocationCategory.CAFE, longitude, latitude, radius);
		return ResponseEntity.ok(response);
	}

	@ApiOperation("카페의 상세 정보를 가져오기")
	@ApiImplicitParam(name = "locationId", value = "카페의 Id", required = true, dataType = "long", example = "1")
	@GetMapping("/api/v1/map/cafes/{locationId}")
	public ResponseEntity<LocationDetailResponse> getCafeById(@PathVariable("locationId") long locationId,
		@UserValidation Users user) {
		LocationDetailResponse dto =
			locationService.getLocationById(LocationCategory.CAFE, locationId, user.getId());
		return ResponseEntity.ok(dto);
	}

	@ApiOperation("현재 위치 기준으로 반경(radius) 내 음식점 정보를 가져오기")
	@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "radius", value = "반경", dataType = "Integer", example = "0", defaultValue = DEFAULT_RADIUS)
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success")
	})
	@GetMapping("/api/v1/map/restaurants")
	public ResponseEntity<LocationResponse> getRestaurants(@UserValidation Users user,
		@NotNull @RequestParam double longitude,
		@NotNull @RequestParam double latitude,
		@RequestParam(required = false, defaultValue = DEFAULT_RADIUS) int radius) {
		LocationResponse response =
			locationService.getLocations(false, LocationCategory.RESTAURANT, longitude, latitude, radius);
		return ResponseEntity.ok(response);
	}

	@ApiOperation("현재 위치 기준으로 반경(radius) 내 음식점의 핀 정보를 가져오기")
	@ApiImplicitParams(value = {
		@ApiImplicitParam(name = "longitude", value = "경도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "latitude", value = "위도", required = true, dataType = "long", example = "1"),
		@ApiImplicitParam(name = "radius", value = "반경", dataType = "Integer", example = "0", defaultValue = DEFAULT_RADIUS)
	})
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "success")
	})
	@GetMapping("/api/v1/map/restaurants/pin")
	public ResponseEntity<LocationResponse> getRestaurantsPin(@UserValidation Users user,
		@NotNull @RequestParam double longitude, @NotNull @RequestParam double latitude,
		@RequestParam(required = false, defaultValue = DEFAULT_RADIUS) int radius) {
		LocationResponse response =
			locationService.getLocations(false, LocationCategory.RESTAURANT, longitude, latitude, radius);
		return ResponseEntity.ok(response);
	}

	@ApiOperation("음식점의 상세 정보를 가져오기")
	@ApiImplicitParam(name = "locationId", value = "음식점의 Id", required = true, dataType = "long", example = "1")
	@GetMapping("/api/v1/map/restaurants/{locationId}")
	public ResponseEntity<LocationDetailResponse> getRestaurantById(@UserValidation Users user,
		@PathVariable("locationId") long locationId) {
		LocationDetailResponse dto =
			locationService.getLocationById(LocationCategory.CAFE, locationId, user.getId());
		return ResponseEntity.ok(dto);
	}
}
