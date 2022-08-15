package com.workat.api.accommodation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.accommodation.dto.response.TagsResponse;
import com.workat.api.accommodation.service.CollectAccommodationTagService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Accommodation Tag API")
@RequiredArgsConstructor
@RestController
public class AccommodationTagController {

	private final CollectAccommodationTagService collectAccommodationTagService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Review Tag 복수건 조회", notes = "숙소 리뷰 태그들을 조회합니다.")
	@GetMapping("/api/v1/tags/accommodationReview")
	public ResponseEntity<TagsResponse> getAccommodationReviewTags() {
		TagsResponse response = collectAccommodationTagService.collectReviewTags();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Info Tag 복수건 조회", notes = "숙소 정보 태그들을 조회합니다.")
	@GetMapping("/api/v1/tags/accommodationInfo")
	public ResponseEntity<TagsResponse> getAccommodationInfoTags() {
		TagsResponse response = collectAccommodationTagService.collectInfoTags();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
