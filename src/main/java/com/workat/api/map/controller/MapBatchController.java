package com.workat.api.map.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.service.LocationService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RequiredArgsConstructor
@RestController
public class MapBatchController {

	private final LocationService locationService;

	@PostMapping("/api/v1/locations/trigger")
	public long updateLocation() {
		return locationService.updateLocations();
	}
}
