package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.LocationResponse;
import com.workat.api.map.dto.LocationUpdateRequest;
import com.workat.api.map.service.LocationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LocationBatchController {

	private final LocationService locationService;

	@PostMapping("/api/v1/location/trigger")
	public void updateLocation(@RequestBody LocationUpdateRequest request) {
		locationService.updateLocations(request);
	}
}
