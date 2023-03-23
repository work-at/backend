package com.workat.api.review.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewWithUserDto;
import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.review.entity.BaseReview;
import com.workat.domain.review.entity.CafeReview;
import com.workat.domain.review.entity.RestaurantReview;
import com.workat.domain.review.repository.BaseReviewRepository;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagSummaryDto;
import com.workat.domain.tag.review.CafeReviewTag;
import com.workat.domain.tag.review.RestaurantReviewTag;
import com.workat.domain.tag.review.ReviewTag;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UsersRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final UsersRepository usersRepository;

	private final BaseReviewRepository baseReviewRepository;

	private final LocationRepository locationRepository;

	public List<TagCountDto> getLocationReviews(long locationId, LocationType type) {
		Location findLocation = locationRepository.findById(locationId).orElseThrow(() -> {
			throw new NotFoundException("location not exist (userId: " + locationId + ")");
		});

		final List<? extends BaseReview> reviews = baseReviewRepository.findAllByTypeAndLocation(type, findLocation);

		final HashMap<ReviewTag, Long> reviewCountMap = convertReviewCountMap(reviews);
		return reviewCountMap.entrySet().stream()
			.map(tag -> TagCountDto.of(TagSummaryDto.of(tag.getKey()), tag.getValue()))
			.sorted(Comparator
				.comparingLong(TagCountDto::getCount)
				.reversed())
			.collect(toList());
	}

	public ReviewWithUserDto getLocationReviewsWithUser(long userId, long locationId, LocationType type) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new NotFoundException("user not exist (userId: " + userId + ")");
		});

		Location findLocation = locationRepository.findById(locationId).orElseThrow(() -> {
			throw new NotFoundException("location not exist (userId: " + locationId + ")");
		});

		final List<? extends BaseReview> reviews = baseReviewRepository.findAllByTypeAndLocation(type, findLocation);

		final long userCount = countDistinctUserByLocationId(locationId, type);
		final HashMap<ReviewTag, Long> reviewCountMap = convertReviewCountMap(reviews);
		final List<ReviewDto> sortedReviewDtos = getSortedReviewDtos(reviewCountMap);

		final boolean userReviewed = checkUserReviewed(reviews, userId);

		return ReviewWithUserDto.of(
			sortedReviewDtos,
			userReviewed,
			userCount
		);
	}

	private <T extends BaseReview> HashMap<ReviewTag, Long> convertReviewCountMap(List<T> reviews) {

		return reviews.stream()
			.collect(groupingBy(BaseReview::getReviewTag, HashMap::new, counting()));
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

	/*
	 * Get Location Reviewed User Count
	 */
	@Transactional(readOnly = true)
	public int countDistinctUserByLocationId(long locationId, LocationType type) {
		Location findLocation = locationRepository.findById(locationId).orElseThrow(() -> {
			throw new NotFoundException("location not exist (userId: " + locationId + ")");
		});

		return baseReviewRepository.getLocationReviewedUserCount(findLocation, type);
	}

	/*
	 * Add Location Review Function
	 */
	@Transactional
	public void addLocationReview(long userId, long locationId, ReviewRequest request) {
		Users findUser = usersRepository.findById(userId).orElseThrow(() -> {
			throw new NotFoundException("user not exist (userId: " + userId + ")");
		});

		Location findLocation = locationRepository.findById(locationId).orElseThrow(() -> {
			throw new NotFoundException("location not exist (userId: " + locationId + ")");
		});

		List<? extends BaseReview> findBaseReviews = baseReviewRepository.findAllByUserAndLocationAndType(findUser, findLocation, request.getCategory());

		if (!findBaseReviews.isEmpty()) {
			throw new ConflictException("There is already a review posted with this user id.");
		}

		if (request.getCategory() == LocationType.CAFE) {
			final List<CafeReview> cafeReviews = request.getReviewTags().stream()
				.map(reviewTag -> CafeReview.of(request.getCategory(), findUser, findLocation, (CafeReviewTag) reviewTag))
				.collect(toList());

			baseReviewRepository.saveAll(cafeReviews);
		} else if (request.getCategory() == LocationType.RESTAURANT) {
			final List<RestaurantReview> RestaurantReviews = request.getReviewTags().stream()
				.map(reviewTag -> RestaurantReview.of(request.getCategory(), findUser, findLocation, (RestaurantReviewTag) reviewTag))
				.collect(toList());

			baseReviewRepository.saveAll(RestaurantReviews);
		} else {
			throw new RuntimeException("");
		}
	}
}
