package com.workat.api.review.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.accommodation.dto.response.TagsResponse;
import com.workat.api.review.dto.ReviewTypeDto;
import com.workat.api.review.dto.response.ReviewTypeListResponse;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.CafeReviewType;
import com.workat.domain.tag.FoodReviewType;

import com.workat.domain.tag.dto.TagContentDto;
import com.workat.domain.tag.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectReviewTypeService {

	public TagsResponse collectCafeReviewTypes() {
		List<TagDto> list = CafeReviewType.ALL
			.stream()
			.map(TagContentDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}

	public TagsResponse collectFoodReviewTypes() {
		List<TagDto> list = FoodReviewType.ALL
			.stream()
			.map(TagContentDto::of)
			.collect(Collectors.toList());

		return TagsResponse.of(list);
	}
}
