package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.LocationDto;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.map.service.LocationService;
import com.workat.domain.map.entity.LocationCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LocationController {

	private final LocationService locationService;

	@GetMapping("/api/v1/map/cafes")
	public ResponseEntity<LocationResponse> getCafes(@RequestParam double longitude, @RequestParam double latitude,
		@RequestParam int radius, @RequestParam int page) {
		LocationResponse response =
			locationService.getLocations(LocationCategory.CAFE, longitude, latitude, radius, page);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/cafes/{locationId}")
	public ResponseEntity<LocationDto> getCafeById(@PathVariable("locationId") long locationId) {
		LocationDto dto =
			locationService.getLocationById(LocationCategory.CAFE, locationId);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/api/v1/map/restaurants")
	public ResponseEntity<LocationResponse> getRestaurants(@RequestParam double longitude,
		@RequestParam double latitude,
		@RequestParam int radius, @RequestParam int page) {
		LocationResponse response =
			locationService.getLocations(LocationCategory.RESTAURANT, longitude, latitude, radius, page);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/restaurants/{locationId}")
	public ResponseEntity<LocationDto> getRestaurantById(@PathVariable("locationId") long locationId) {
		LocationDto dto =
			locationService.getLocationById(LocationCategory.RESTAURANT, locationId);
		return ResponseEntity.ok(dto);
	}
}
