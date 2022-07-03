package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.LocationRequest;
import com.workat.api.map.dto.LocationResponse;
import com.workat.api.map.service.LocationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LocationController {

	private final LocationService locationService;

	@GetMapping("/api/v1/location/{category}")
	public ResponseEntity<LocationResponse> getLocation(
		@PathVariable("category") String category, @RequestBody LocationRequest request) {
		LocationResponse response = locationService.getLocations(category, request);
		return ResponseEntity.ok(response);
	}
}
