package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.LocationDetailDto;
import com.workat.api.map.dto.response.CafeDetailResponse;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.map.service.LocationService;
import com.workat.api.review.service.ReviewService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LocationController {

	private final LocationService locationService;

	private final ReviewService reviewService;

	@GetMapping("/api/v1/map/cafes")
	public ResponseEntity<LocationResponse> getCafes(@RequestParam double longitude, @RequestParam double latitude,
		@RequestParam int radius) {
		LocationResponse response = locationService.getLocations(false, LocationCategory.CAFE, longitude, latitude,
			radius);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/cafes/pin")
	public ResponseEntity<LocationResponse> getCafesPin(@RequestParam double longitude, @RequestParam double latitude,
		@RequestParam int radius) {
		LocationResponse response =
			locationService.getLocations(true, LocationCategory.CAFE, longitude, latitude, radius);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/cafes/{locationId}")
	public ResponseEntity<CafeDetailResponse> getCafeById(@PathVariable("locationId") long locationId,
		@UserValidation Users user) {
		CafeDetailResponse dto =
			locationService.getCafeById(LocationCategory.CAFE, locationId, user);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/api/v1/map/restaurants")
	public ResponseEntity<LocationResponse> getRestaurants(@RequestParam double longitude,
		@RequestParam double latitude,
		@RequestParam int radius) {
		LocationResponse response =
			locationService.getLocations(false, LocationCategory.RESTAURANT, longitude, latitude, radius);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/restaurants/pin")
	public ResponseEntity<LocationResponse> getRestaurantsPin(@RequestParam double longitude,
		@RequestParam double latitude,
		@RequestParam int radius) {
		LocationResponse response =
			locationService.getLocations(false, LocationCategory.RESTAURANT, longitude, latitude, radius);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/restaurants/{locationId}")
	public ResponseEntity<LocationDetailDto> getRestaurantById(@PathVariable("locationId") long locationId) {
		LocationDetailDto dto =
			locationService.getLocationById(LocationCategory.RESTAURANT, locationId);
		return ResponseEntity.ok(dto);
	}
}
