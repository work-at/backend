package com.workat.api.accommodation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.accommodation.dto.response.AccommodationsResponse;
import com.workat.api.accommodation.service.AccommodationService;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "Accommodation Api")
@RequiredArgsConstructor
@RestController
public class AccommodationController {

	private final AccommodationService accommodationService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation("Accommodation 복수건 조회")
	@GetMapping("/api/v1/accommodations")
	public ResponseEntity<AccommodationsResponse> getAccommodations(
		@ApiParam(value = "지역", example = "SEOUL", type = "string")
		@RequestParam(required = false) RegionType region,
		@ApiParam(value = "info tag name", example = "NEAR_FOREST", type = "string")
		@RequestParam(required = false) AccommodationInfoTag infoTagName,
		@ApiParam(value = "top review tag name", example = "WIFI", type = "string")
		@RequestParam(required = false) AccommodationReviewTag topReviewTagName,
		@ApiParam(value = "페이지 번호", example = "0", type = "integer")
		@RequestParam(required = false, defaultValue = "0") int pageNumber,
		@ApiParam(value = "페이지 사이즈", example = "10", type = "integer")
		@RequestParam(required = false, defaultValue = "10") int pageSize
	) {
		AccommodationsResponse response = accommodationService.getAccommodations(
			region, infoTagName, topReviewTagName, pageNumber, pageSize);

		return ResponseEntity.ok(response);
	}
}
