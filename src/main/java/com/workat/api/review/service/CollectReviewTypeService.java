package com.workat.api.review.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.review.dto.ReviewTypeListResponse;
import com.workat.api.review.dto.ReviewTypeResponse;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.review.FoodReviewType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectReviewTypeService {

	public ReviewTypeListResponse collectCafeReviewTypes() {
		List<ReviewTypeResponse> list = Arrays.stream(CafeReviewType.values())
			.map(ReviewTypeResponse::of)
			.collect(Collectors.toList());

		return ReviewTypeListResponse.of(list);
	}

	public ReviewTypeListResponse collectFoodReviewTypes() {
		List<ReviewTypeResponse> list = Arrays.stream(FoodReviewType.values())
			.map(ReviewTypeResponse::of)
			.collect(Collectors.toList());

		return ReviewTypeListResponse.of(list);
	}
}
