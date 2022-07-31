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

import com.workat.api.review.dto.LocationReviewDto;
import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.common.exception.BadRequestException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.review.BaseReviewType;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.review.FoodReviewType;
import com.workat.domain.review.entity.BaseReview;
import com.workat.domain.review.entity.CafeReview;
import com.workat.domain.review.entity.RestaurantReview;
import com.workat.domain.review.repository.CafeReviewRepository;
import com.workat.domain.review.repository.RestaurantReviewRepository;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final CafeReviewRepository cafeReviewRepository;

	private final RestaurantReviewRepository restaurantReviewRepository;

	private final LocationRepository locationRepository;

	public List<ReviewDto> getLocationReviews(long locationId, LocationCategory category) {

		final List<? extends BaseReview> reviews = getReviewsByCategory(locationId, category);

		final HashMap<BaseReviewType, Long> reviewCountMap = convertReviewCountMap(reviews);
		final List<ReviewDto> sortedReviewDtos = getSortedReviewDtos(reviewCountMap);

		return sortedReviewDtos;
	}

	public LocationReviewDto getLocationReviewsWithUser(long locationId, LocationCategory category, long userId) {

		final List<? extends BaseReview> reviews = getReviewsByCategory(locationId, category);

		final HashMap<BaseReviewType, Long> reviewCountMap = convertReviewCountMap(reviews);
		final List<ReviewDto> sortedReviewDtos = getSortedReviewDtos(reviewCountMap);

		final boolean userReviewed = checkUserReviewed(reviews, userId);

		return LocationReviewDto.of(
			sortedReviewDtos,
			userReviewed
		);
	}

	@Transactional(readOnly = true)
	public List<? extends BaseReview> getReviewsByCategory(long locationId, LocationCategory category) {

		if (category == LocationCategory.CAFE) {
			return cafeReviewRepository.findAllByLocation_Id(locationId);
		}

		return restaurantReviewRepository.findAllByLocation_Id(locationId);
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

	private boolean checkUserReviewed(List<? extends BaseReview> reviews, long userId) {
		final Optional<? extends BaseReview> reviewMatchedUser = reviews.stream()
			.filter(review -> review.getUser().getId() == userId)
			.findFirst();

		return reviewMatchedUser.isPresent();
	}

	@Transactional
	public void addCafeReview(long locationId, ReviewRequest reviewRequest, Users user) {

		final Location location = locationRepository.findById(locationId)
			.orElseThrow(() -> new NotFoundException("No location found for the id"));

		final Optional<CafeReview> optionalCafeReview = cafeReviewRepository.findByLocation_IdAndUser_Id(
			locationId,
			user.getId());

		if (optionalCafeReview.isPresent()) {
			throw new BadRequestException("There is already a review posted with this user id.");
		}

		final HashSet<String> reviewTypeNames = reviewRequest.getReviewTypeNames();

		final List<CafeReview> cafeReviews = reviewTypeNames.stream()
			.map(CafeReviewType::of)
			.map(reviewType -> CafeReview.of(reviewType, location, user))
			.collect(toList());

		cafeReviewRepository.saveAll(cafeReviews);
	}

	@Transactional
	public void addRestaurantReview(long locationId, ReviewRequest reviewRequest, Users user) {

		final Location location = locationRepository.findById(locationId)
			.orElseThrow(() -> new NotFoundException("No location found for the id"));

		final Optional<RestaurantReview> optionalRestaurantReview = restaurantReviewRepository.findByLocation_IdAndUser_Id(
			locationId,
			user.getId());

		if (optionalRestaurantReview.isPresent()) {
			throw new BadRequestException("There is already a review posted with this user id.");
		}

		final HashSet<String> reviewTypeNames = reviewRequest.getReviewTypeNames();

		final List<RestaurantReview> RestaurantReviews = reviewTypeNames.stream()
			.map(FoodReviewType::of)
			.map(reviewType -> RestaurantReview.of(reviewType, location, user))
			.collect(toList());

		restaurantReviewRepository.saveAll(RestaurantReviews);
	}
}
