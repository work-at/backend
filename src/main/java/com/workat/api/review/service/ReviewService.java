package com.workat.api.review.service;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewsDto;
import com.workat.api.review.dto.request.CafeReviewRequest;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.review.BaseReviewType;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.review.entity.BaseReview;
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
	public ReviewsDto getLocationReviews(long locationId, Users user) {
		final List<CafeReview> cafeReviews = cafeReviewRepository.findAllByLocation_Id(locationId);

		HashMap<BaseReviewType, Long> reviewCountMap = convertReviewCountMap(cafeReviews);
		final List<ReviewDto> sortedReviewDtos = getSortedReviewDtos(reviewCountMap);

		final boolean userReviewed = checkUserReviewed(cafeReviews, user);

		return ReviewsDto.of(
			sortedReviewDtos,
			userReviewed
		);
	}

	private <T extends BaseReview> HashMap<BaseReviewType, Long> convertReviewCountMap(List<T> reviews) {

		final HashMap<BaseReviewType, Long> reviewCountMap = reviews.stream()
			.collect(groupingBy(BaseReview::getReviewType, HashMap::new, counting()));

		return reviewCountMap;
	}

	private <T extends BaseReviewType> List<ReviewDto> getSortedReviewDtos(Map<T, Long> map) {

		return map.entrySet()
			.stream()
			.map(entry -> ReviewDto.of(
				entry.getKey(),
				entry.getValue()))
			.sorted(Comparator.comparingLong(ReviewDto::getCount) // count 역순으로 정렬
				.reversed())
			.collect(toList());
	}

	private boolean checkUserReviewed(List<CafeReview> cafeReviews, Users user) {
		final Optional<CafeReview> reviewMatchedUser = cafeReviews.stream()
			.filter(review -> review.getUser().getId() == user.getId())
			.findFirst();

		return reviewMatchedUser.isPresent();
	}

	@Transactional
	public void addCafeReview(long locationId, CafeReviewRequest cafeReviewRequest, Users user) {

		final Location location = locationRepository.findById(locationId)
			.orElseThrow(() -> new NotFoundException("No location found for the id"));

		final Optional<CafeReview> optionalCafeReview = cafeReviewRepository.findByLocation_IdAndUser_Id(
			locationId,
			user.getId());

		if (optionalCafeReview.isPresent()) {
			throw new BadRequestException("There is already a review posted with this user id.");
		}

		final HashSet<String> reviewTypeNames = cafeReviewRequest.getReviewTypeNames();

		final List<CafeReview> cafeReviews = reviewTypeNames.stream()
			.map(name -> CafeReviewType.of(name))
			.map(reviewType -> CafeReview.of(reviewType, location, user))
			.collect(toList());

		cafeReviewRepository.saveAll(cafeReviews);
	}
}
