package com.workat.api.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.api.review.service.ReviewService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/api/v1/map/cafes/{locationId}/reviews")
	public ResponseEntity addCafeReview(@PathVariable long locationId,
		@RequestBody ReviewRequest reviewRequest,
		@UserValidation Users user
	) {
		reviewService.addCafeReview(locationId, reviewRequest, user);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/api/v1/map/locations/{locationId}/reviews")
	public ResponseEntity addLocationReview(@PathVariable long locationId,
		@RequestBody ReviewRequest reviewRequest,
		@UserValidation Users user
	) {
		reviewService.addRestaurantReview(locationId, reviewRequest, user);

		return ResponseEntity.ok().build();
	}
}
