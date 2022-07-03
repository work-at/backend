package com.workat.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.review.dto.ReviewTypeListResponse;
import com.workat.api.review.service.CollectReviewTypeService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class CollectReviewEnumController {

	private final CollectReviewTypeService collectReviewEnumService;

	@GetMapping("/types/review/cafe")
	public ResponseEntity<ReviewTypeListResponse> getCafeTypesReviewTypes() {
		ReviewTypeListResponse response = collectReviewEnumService.collectCafeReviewTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/types/review/restaurant")
	public ResponseEntity<ReviewTypeListResponse> getRestaurantReviewTypes() {
		ReviewTypeListResponse response = collectReviewEnumService.collectRestaurantReviewTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
