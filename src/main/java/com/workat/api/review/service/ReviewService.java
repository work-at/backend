package com.workat.api.review.service;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.review.dto.CafeReviewDto;
import com.workat.api.review.dto.request.CafeReviewRequest;
import com.workat.api.review.dto.response.CafeReviewsResponse;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.repository.LocationRepository;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.review.entity.CafeReview;
import com.workat.domain.review.repository.CafeReviewRepository;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final CafeReviewRepository cafeReviewRepository;

	private final LocationRepository locationRepository;

	@Transactional(readOnly = true)
	public CafeReviewsResponse getCafeReviews(long locationId) {

		final List<CafeReview> cafeReviews = cafeReviewRepository.findAllByLocation_Id(locationId);

		final Map<CafeReviewType, Long> reviewCountMap = cafeReviews.stream()
																	.collect(
																		groupingBy(CafeReview::getReviewType,
																			counting()));

		final List<CafeReviewDto> cafeReviewDtos = reviewCountMap.entrySet()
																 .stream()
																 .map(entry ->
																	 CafeReviewDto.of(
																		 entry.getKey(),
																		 entry.getValue()))
																 .sorted(
																	 // count 역순으로 정렬
																	 Comparator.comparingLong(CafeReviewDto::getCount)
																			   .reversed())
																 .collect(toList());

		return CafeReviewsResponse.of(cafeReviewDtos);
	}

	@Transactional
	public void addCafeReview(long locationId, CafeReviewRequest cafeReviewRequest, Users user) {

		final Location location = locationRepository.findById(locationId)
													.orElseThrow(
														() -> new NotFoundException(
															"No location found for the id"));

		final Optional<CafeReview> optionalCafeReview = cafeReviewRepository.findByLocation_IdAndUser_Id(
			locationId,
			user.getId());

		if (optionalCafeReview.isPresent()) {
			throw new BadRequestException("There is already a review posted with this user id.");
		}

		final List<String> reviewTypeNames = cafeReviewRequest.getReviewTypeNames();

		final List<CafeReview> cafeReviews = reviewTypeNames.stream()
															.map(name -> CafeReviewType.of(name))
															.distinct()
															.map(
																reviewType -> CafeReview.of(reviewType, location,
																	user))
															.collect(toList());

		cafeReviewRepository.saveAll(cafeReviews);
	}
}