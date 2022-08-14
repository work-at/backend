package com.workat.api.review.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.review.dto.ReviewTypeDto;
import com.workat.api.review.dto.response.ReviewTypeListResponse;
import com.workat.domain.tag.CafeReviewType;
import com.workat.domain.tag.FoodReviewType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectReviewTypeService {

	public ReviewTypeListResponse collectCafeReviewTypes() {
		List<ReviewTypeDto> list = Arrays.stream(CafeReviewType.values())
			.map(ReviewTypeDto::of)
			.collect(Collectors.toList());

		return ReviewTypeListResponse.of(list);
	}

	public ReviewTypeListResponse collectFoodReviewTypes() {
		List<ReviewTypeDto> list = Arrays.stream(FoodReviewType.values())
			.map(ReviewTypeDto::of)
			.collect(Collectors.toList());

		return ReviewTypeListResponse.of(list);
	}
}
