package com.workat.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.api.review.service.ReviewService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Review Api")
@RequiredArgsConstructor
@RestController
public class ReviewController {

	private final ReviewService reviewService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "add cafe review", notes = "카페 리뷰 추가")
	@ApiImplicitParam(name = "locationId", value = "카페의 Id", required = true, dataType = "long", example = "1")
	@PostMapping("/api/v1/map/cafes/{locationId}/reviews")
	public ResponseEntity addCafeReview(@PathVariable long locationId,
		@RequestBody ReviewRequest reviewRequest,
		@UserValidation Users user
	) {
		reviewService.addCafeReview(locationId, reviewRequest, user);

		return ResponseEntity.ok().build();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "add restaurant review", notes = "식당 리뷰 추가")
	@ApiImplicitParam(name = "locationId", value = "식당의 Id", required = true, dataType = "long", example = "2")
	@PostMapping("/api/v1/map/restaurants/{locationId}/reviews")
	public ResponseEntity addRestaurantReview(@PathVariable long locationId,
		@RequestBody ReviewRequest reviewRequest,
		@UserValidation Users user
	) {
		reviewService.addRestaurantReview(locationId, reviewRequest, user);

		return ResponseEntity.ok().build();
	}
}
