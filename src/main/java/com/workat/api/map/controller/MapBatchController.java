package com.workat.api.map.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.request.LocationTriggerRequest;
import com.workat.api.map.service.LocationService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RequiredArgsConstructor
@RestController
public class MapBatchController {

	private final LocationService locationService;

	@PostMapping("/api/v1/locations/trigger")
	public void updateLocation() {
		locationService.updateLocations();
	}
}
