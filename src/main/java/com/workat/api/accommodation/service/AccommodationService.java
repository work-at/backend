package com.workat.api.accommodation.service;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.accommodation.dto.AccommodationCuration;
import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationDto;
import com.workat.api.accommodation.dto.AccommodationReviewDto;
import com.workat.api.accommodation.dto.request.AccommodationReviewRequest;
import com.workat.api.accommodation.dto.response.AccommodationCurationsResponse;
import com.workat.api.accommodation.dto.response.AccommodationResponse;
import com.workat.api.accommodation.dto.response.AccommodationsResponse;
import com.workat.common.exception.ConflictException;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.UrlUtils;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.AccommodationInfo;
import com.workat.domain.accommodation.entity.AccommodationReview;
import com.workat.domain.accommodation.repository.AccommodationInfoRepository;
import com.workat.domain.accommodation.repository.AccommodationRepository;
import com.workat.domain.accommodation.repository.AccommodationReviewRepository;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagDto;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	private final AccommodationReviewRepository accommodationReviewRepository;

	private final AccommodationInfoRepository accommodationInfoRepository;

	@Transactional(readOnly = true)
	public AccommodationsResponse getAccommodations(
		String baseUrl,
		RegionType region,
		AccommodationInfoTag infoTag,
		AccommodationReviewTag reviewTag,
		int pageNumber,
		int pageSize
	) {

		final PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id");

		final Page<Accommodation> accommodationPages = getAccommodationPagesWithFilter(
			pageRequest,
			region,
			infoTag,
			reviewTag);

		final List<AccommodationDto> accommodationDtos = convertAccommodationDto(baseUrl,
			accommodationPages.getContent());

		return AccommodationsResponse.of(
			accommodationDtos,
			accommodationPages.getNumber(),
			accommodationPages.getSize(),
			accommodationPages.getTotalElements()
		);
	}

	@Transactional(readOnly = true)
	public AccommodationResponse getAccommodation(String baseUrl, long accommodationId, long userId) {

		final Accommodation accommodation = accommodationRepository.findById(accommodationId)
			.orElseThrow(() -> {
				throw new NotFoundException("accommodation is not found");
			});

		final List<AccommodationInfo> accommodationInfos = accommodationInfoRepository.findAllByAccommodation(
			accommodation);

		final AccommodationDetailDto accommodationDetailDto = convertAccommodationDetailDto(baseUrl, accommodation,
			accommodationInfos);

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

	@Transactional
	public void addAccommodationReview(long accommodationId, AccommodationReviewRequest reviewRequest, Users user) {

		final Accommodation accommodation = accommodationRepository.findById(accommodationId)
			.orElseThrow(() -> new NotFoundException("No accommodation found for the id"));

		final List<AccommodationReview> optionalAccommodationReviews = accommodationReviewRepository.findAllByAccommodation_IdAndUser_Id(
			accommodation.getId(),
			user.getId());

		if (!optionalAccommodationReviews.isEmpty()) {
			throw new ConflictException("There is already a review posted with this user id.");
		}

		final HashSet<String> reviewTagNames = reviewRequest.getTagNames();

		final List<AccommodationReview> accommodationReviews = reviewTagNames.stream()
			.map(AccommodationReviewTag::of)
			.map(tag -> AccommodationReview.of(tag, accommodation, user))
			.collect(toList());

		accommodationReviewRepository.saveAll(accommodationReviews);
	}

	@Transactional(readOnly = true)
	public AccommodationCurationsResponse getAccommodationCurations() {

		List<Accommodation> accommodations = accommodationRepository.findAllByRandom(5);

		return AccommodationCurationsResponse.of(
			accommodations.stream()
				.map(accommodation -> AccommodationCuration.of(
					accommodation.getId(),
					accommodation.getName(),
					accommodation.getRegionType(),
					accommodation.getImgUrl()
				))
				.collect(toList())
		);
	}

	@Transactional(readOnly = true)
	public Page<Accommodation> getAccommodationPagesWithFilter(
		PageRequest pageRequest,
		RegionType region,
		AccommodationInfoTag infoTag,
		AccommodationReviewTag reviewTag
	) {
		// TODO: 좀 더 깔끔한 방법 생각해보기
		if (region != null && infoTag != null) {
			return accommodationRepository.findAllByRegionAndInfoTag(region.getValue(),
				infoTag.getName(),
				pageRequest);
		}

		if (region == null && infoTag != null) {
			return accommodationRepository.findAllByInfoTag(infoTag.getName(), pageRequest);
		}

		if (infoTag == null && region != null) {
			return accommodationRepository.findAllByRegionType(region, pageRequest);
		}

		return accommodationRepository.findAll(pageRequest);
	}

	@Transactional(readOnly = true)
	public List<AccommodationDto> getAccommodationsWithName(String baseUrl, String name) {

		// TODO: getAccommodations 와 통합해보기
		final List<Accommodation> accommodations = accommodationRepository.findAllByNameContaining(name);

		final List<AccommodationDto> accommodationDtos = convertAccommodationDto(baseUrl, accommodations);

		return accommodationDtos;
	}

	@Transactional(readOnly = true)
	public List<AccommodationDto> convertAccommodationDto(String baseUrl, List<Accommodation> accommodations) {

		return accommodations.stream()
			.map(accommodation -> {

				// TODO: 효율적인 방법으로 고치기
				LinkedHashSet<TagDto> tagsDtoSet = AccommodationReviewTag.ALL.stream()
					.map(tag -> {
						Long count = accommodationReviewRepository.countByTag(tag);

						return TagCountDto.of(TagDto.of(tag), count);
					})
					.sorted(Comparator
						.comparingLong(TagCountDto::getCount)
						.reversed())
					.limit(3)
					.map(TagCountDto::getTag)
					.collect(toCollection(LinkedHashSet::new));

				return AccommodationDto.of(
					accommodation.getId(),
					accommodation.getName(),
					accommodation.getPrice(),
					baseUrl + accommodation.getThumbnailImgUrl() + ".png",
					tagsDtoSet);
			}).collect(toList());
	}

	private AccommodationDetailDto convertAccommodationDetailDto(String baseUrl,
		Accommodation accommodation, List<AccommodationInfo> accommodationInfos) {

		return AccommodationDetailDto.builder()
			.id(accommodation.getId())
			.name(accommodation.getName())
			.imgUrl(baseUrl + accommodation.getImgUrl() + ".png")
			.price(accommodation.getPrice())
			.phone(accommodation.getPhone())
			.roadAddressName(accommodation.getRoadAddressName())
			.placeUrl(accommodation.getPlaceUrl())
			.relatedUrl(accommodation.getRelatedUrl())
			.infoTags(accommodationInfos.stream()
				.map(AccommodationInfo::getTag)
				.map(TagDto::of)
				.collect(toCollection(HashSet::new)))
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
