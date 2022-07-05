package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.request.LocationRequest;
import com.workat.api.map.dto.response.LocationResponse;
import com.workat.api.map.service.LocationService;
import com.workat.domain.map.entity.LocationCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LocationController {

	private final LocationService locationService;

	@GetMapping("/api/v1/map/cafes")
	public ResponseEntity<LocationResponse> getCafes(@RequestBody LocationRequest request) {
		LocationResponse response = locationService.getLocations(LocationCategory.CAFE, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/restaurants")
	public ResponseEntity<LocationResponse> getRestaurants(@RequestBody LocationRequest request) {
		LocationResponse response = locationService.getLocations(LocationCategory.RESTAURANT, request);
		return ResponseEntity.ok(response);
	}
}
