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
	public ResponseEntity addCafeReview(@UserValidation Users user,
		@PathVariable long locationId,
		@RequestBody ReviewRequest request
	) {
		request.validate();
		reviewService.addLocationReview(user.getId(), locationId, request);

		return ResponseEntity.ok().build();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "add restaurant review", notes = "식당 리뷰 추가")
	@ApiImplicitParam(name = "locationId", value = "식당의 Id", required = true, dataType = "long", example = "2")
	@PostMapping("/api/v1/map/restaurants/{locationId}/reviews")
	public ResponseEntity addRestaurantReview(@PathVariable long locationId,
		@RequestBody ReviewRequest request,
		@UserValidation Users user
	) {
		request.validate();
		reviewService.addLocationReview(user.getId(), locationId, request);

		return ResponseEntity.ok().build();
	}

	/*
	 * TODO
	 * 1. 불필요한 Api를 Enum으로 합친다.
	 * 2. 현재는 Front 작업이 불가능하기때문에 둘다 살려두도록한다.
	 */
	@PostMapping("/api/v1/map/locations/{locationId}/reviews")
	public ResponseEntity<Void> addLocationReview(@UserValidation Users user, @PathVariable long locationId, @RequestBody ReviewRequest request) {
		request.validate();
		reviewService.addLocationReview(user.getId(), locationId, request);
		return ResponseEntity.ok().build();
	}
}
