package com.workat.api.review.service;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewWithUserDto;
import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.locationReview.entity.BaseReview;
import com.workat.domain.locationReview.entity.CafeReview;
import com.workat.domain.locationReview.entity.RestaurantReview;
import com.workat.domain.locationReview.repository.CafeReviewRepository;
import com.workat.domain.locationReview.repository.RestaurantReviewRepository;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.tag.ReviewTag;
import com.workat.domain.tag.CafeReviewType;
import com.workat.domain.tag.FoodReviewType;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagSummaryDto;
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

	public List<TagCountDto> getLocationReviews(long locationId, LocationCategory category) {

		final List<? extends BaseReview> reviews = getReviewsByCategory(locationId, category);

		final HashMap<ReviewTag, Long> reviewCountMap = convertReviewCountMap(reviews);
		return reviewCountMap.entrySet().stream()
			.map(tag -> {
				return TagCountDto.of(TagSummaryDto.of(tag.getKey()), tag.getValue());
			})
			.sorted(Comparator
				.comparingLong(TagCountDto::getCount)
				.reversed())
			.collect(toList());
	}

	public ReviewWithUserDto getLocationReviewsWithUser(long locationId, LocationCategory category, long userId) {

		final List<? extends BaseReview> reviews = getReviewsByCategory(locationId, category);

		final long userCount = countDistinctUserByLocationId(locationId, category);
		final HashMap<ReviewTag, Long> reviewCountMap = convertReviewCountMap(reviews);
		final List<ReviewDto> sortedReviewDtos = getSortedReviewDtos(reviewCountMap);

		final boolean userReviewed = checkUserReviewed(reviews, userId);

		return ReviewWithUserDto.of(
			sortedReviewDtos,
			userReviewed,
			userCount
		);
	}

	@Transactional(readOnly = true)
	public List<? extends BaseReview> getReviewsByCategory(long locationId, LocationCategory category) {

		if (category == LocationCategory.CAFE) {
			return cafeReviewRepository.findAllByLocation_Id(locationId);
		}

		return restaurantReviewRepository.findAllByLocation_Id(locationId);
	}

	private <T extends BaseReview> long countReviewedUser(List<T> reviews) {
		return reviews.stream()
			.map(review -> review.getUser().getId())
			.collect(toSet())
			.size();
	}

	private <T extends BaseReview> HashMap<ReviewTag, Long> convertReviewCountMap(List<T> reviews) {

		final HashMap<ReviewTag, Long> reviewCountMap = reviews.stream()
			.collect(groupingBy(BaseReview::getReviewType, HashMap::new, counting()));

		return reviewCountMap;
	}

	private <T extends ReviewTag> List<ReviewDto> getSortedReviewDtos(Map<T, Long> map) {

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
		return reviews.stream()
			.anyMatch(review -> review.getUser().getId() == userId);
	}

	@Transactional
	public void addCafeReview(long locationId, ReviewRequest reviewRequest, Users user) {

		final Location location = locationRepository.findById(locationId)
			.orElseThrow(() -> new NotFoundException("No location found for the id"));

		final List<CafeReview> optionalCafeReviews = cafeReviewRepository.findAllByLocation_IdAndUser_Id(
			locationId,
			user.getId());

		if (!optionalCafeReviews.isEmpty()) {
			throw new ConflictException("There is already a review posted with this user id.");
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

		final List<RestaurantReview> optionalRestaurantReviews = restaurantReviewRepository.findAllByLocation_IdAndUser_Id(
			locationId,
			user.getId());

		if (!optionalRestaurantReviews.isEmpty()) {
			throw new ConflictException("There is already a review posted with this user id.");
		}

		final HashSet<String> reviewTypeNames = reviewRequest.getReviewTypeNames();

		final List<RestaurantReview> RestaurantReviews = reviewTypeNames.stream()
			.map(FoodReviewType::of)
			.map(reviewType -> RestaurantReview.of(reviewType, location, user))
			.collect(toList());

		restaurantReviewRepository.saveAll(RestaurantReviews);
	}

	@Transactional(readOnly = true)
	public int countDistinctUserByLocationId(long locationId, LocationCategory category){
		if (category == LocationCategory.CAFE) {
			return cafeReviewRepository.countDistinctUserByLocationId(locationId);
		}

		return restaurantReviewRepository.countDistinctUserByLocationId(locationId);
	}
}
