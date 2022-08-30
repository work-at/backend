package com.workat.api.accommodation.service;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationDto;
import com.workat.api.accommodation.dto.AccommodationReviewDto;
import com.workat.api.accommodation.dto.response.AccommodationResponse;
import com.workat.api.accommodation.dto.response.AccommodationsResponse;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.AccommodationReview;
import com.workat.domain.accommodation.repository.AccommodationRepository;
import com.workat.domain.accommodation.repository.AccommodationReviewRepository;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	private final AccommodationReviewRepository accommodationReviewRepository;

	public AccommodationsResponse getAccommodations(
		RegionType region,
		AccommodationInfoTag infoTagName,
		AccommodationReviewTag accommodationReviewTag,
		int pageNumber,
		int pageSize
	) {

		return AccommodationsResponse.of(
			Arrays.asList(AccommodationDto.of(1L, "그랜드워커힐서울", 235000L,
					"https://lh5.googleusercontent.com/p/AF1QipOKO_7oTuHLK31fOjhqp13KompnHRxgi_2_oOVT=w253-h168-k-no",
					Arrays.asList(
						TagDto.of(AccommodationReviewTag.FOCUS),
						TagDto.of(AccommodationReviewTag.SERVE_MEAL),
						TagDto.of(AccommodationReviewTag.WIFI)
					)
				),
				AccommodationDto.of(2L, "씨마크호텔", 135000L,
					"https://lh5.googleusercontent.com/proxy/pxIAn34FA3bpLmWfDBKZe6uTiFdb7JrocuP7tzcLTTWcINIqCKLsuADqZW65VteN0bZ28rWStDjNwGjBNhr4_V8KHjBW7aWhNkORBP3Jw9UFmeqive-omWDVIUh5HwVj29V9wi_7iOoUKcvCG6XduQ6Bl2MYyQ=w253-h184-k-no",
					Arrays.asList(
						TagDto.of(AccommodationReviewTag.WIFI),
						TagDto.of(AccommodationReviewTag.POWER),
						TagDto.of(AccommodationReviewTag.SERVE_MEAL)
					)
				)
			), pageNumber, pageSize, 150
		);
	}

	@Transactional(readOnly = true)
	public AccommodationResponse getAccommodation(long accommodationId, long userId) {

		final Accommodation accommodation = accommodationRepository.findById(accommodationId)
			.orElseThrow(() -> {
				throw new NotFoundException("accommodation is not found");
			});

		final AccommodationDetailDto accommodationDetailDto = convertAccommodationDetailDto(accommodation);

		final List<AccommodationReview> accommodationReviews = accommodationReviewRepository.findAllByAccommodation_Id(
			accommodationId);

		final AccommodationReviewDto accommodationReviewDto = convertAccommodationReviewDto(
			accommodationReviews,
			userId);

		return AccommodationResponse.of(
			accommodationDetailDto,
			accommodationReviewDto
		);
	}

	private AccommodationDetailDto convertAccommodationDetailDto(Accommodation accommodation) {

		return AccommodationDetailDto.builder()
			.id(accommodation.getId())
			.name(accommodation.getName())
			.imgUrl(accommodation.getImgUrl())
			.price(accommodation.getPrice())
			.phone(accommodation.getPhone())
			.roadAddressName(accommodation.getRoadAddressName())
			.placeUrl(accommodation.getPlaceUrl())
			.relatedUrl(accommodation.getRelatedUrl())
			.infoTags(accommodation.getInfoTags().stream()
				.map(TagDto::of)
				.collect(Collectors.toList()))
			.build();
	}

	private AccommodationReviewDto convertAccommodationReviewDto(List<AccommodationReview> reviews, long userId) {

		final HashMap<AccommodationReviewTag, Long> reviewTagMap = convertReviewCountMap(
			reviews);

		final List<TagCountDto> tagCountDtos = convertTagCountDtos(reviewTagMap);

		final HashSet<Long> userIdSet = reviews.stream()
			.map(review ->
				review.getUser().getId())
			.collect(toCollection(HashSet::new));

		final boolean userReviewed = userIdSet.contains(userId);

		final int userCount = userIdSet.size();

		return AccommodationReviewDto.of(
			tagCountDtos,
			userReviewed,
			userCount
		);
	}

	private HashMap<AccommodationReviewTag, Long> convertReviewCountMap(List<AccommodationReview> reviews) {

		// 태그별 카운트
		final HashMap<AccommodationReviewTag, Long> reviewCountMap = reviews.stream()
			.collect(groupingBy(
				AccommodationReview::getTag, HashMap::new, counting()
			));

		return reviewCountMap;
	}

	private List<TagCountDto> convertTagCountDtos(HashMap<AccommodationReviewTag, Long> map) {

		return map.entrySet().stream()
			.map(entry ->
				TagCountDto.of(
					TagDto.of(entry.getKey()),
					entry.getValue()))
			.sorted(Comparator
				.comparingLong(TagCountDto::getCount)
				.reversed() // count 역순으로 정렬
			)
			.collect(Collectors.toList());
	}

}
