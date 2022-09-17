package com.workat.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.accommodation.dto.response.TagsResponse;
import com.workat.api.review.service.CollectReviewTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "리뷰 타입 조회")
@RequiredArgsConstructor
@RestController
public class CollectReviewTypeController {

	private final CollectReviewTypeService collectReviewEnumService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "카페 리뷰 조회", notes = "카페 리뷰 목록을 조회합니다")
	@GetMapping("/api/v1/cafe/review-type")
	public ResponseEntity<TagsResponse> getCafeTypesReviewTypes() {
		TagsResponse response = collectReviewEnumService.collectCafeReviewTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "음식점 리뷰 조회", notes = "음식점 리뷰 목록을 조회합니다")
	@GetMapping("/api/v1/food/review-type")
	public ResponseEntity<TagsResponse> getFoodReviewTypes() {
		TagsResponse response = collectReviewEnumService.collectFoodReviewTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
