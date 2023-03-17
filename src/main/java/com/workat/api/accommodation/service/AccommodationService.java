package com.workat.api.accommodation.service;

import static java.util.stream.Collectors.toList;

import com.workat.api.accommodation.dto.AccommodationCurationDto;
import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationDto;
import com.workat.api.accommodation.dto.AccommodationReviewDto;
import com.workat.api.accommodation.dto.request.AccommodationCreationRequest;
import com.workat.api.accommodation.dto.request.AccommodationReviewRequest;
import com.workat.api.accommodation.dto.response.AccommodationCurationsResponse;
import com.workat.api.accommodation.dto.response.AccommodationResponse;
import com.workat.api.accommodation.dto.response.AccommodationsResponse;
import com.workat.api.accommodation.service.data.AccommodationDataService;
import com.workat.api.user.service.data.UserDataService;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.embed.AccommodationInfo;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReview;
import com.workat.domain.accommodation.entity.review.AccommodationReviewHistory;
import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviationHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.accommodation.repository.AccommodationSearchAndFilterRepository;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.user.entity.Users;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 TODO
 1. Accommodation 생성 로직에서 Review가 없는 경우에 대한 Init 데이터 초기화해서 처리하는 로직 필요
 2. Filter 로직 개선 필요
 */
@RequiredArgsConstructor
@Service
public class AccommodationService {

	private final UserDataService userDataService;

	private final AccommodationDataService accommodationDataService;

	private final AccommodationSearchAndFilterRepository accommodationSearchAndFilterRepository;

	@Transactional
	public Accommodation createAccommodation(AccommodationCreationRequest request) {
		Accommodation accommodation = Accommodation.builder()
			.regionType(request.getRegionType())
			.name(request.getName())
			.imgUrl(request.getImgUrl())
			.thumbnailImgUrl(request.getThumbnailUrl())
			.price(request.getPrice())
			.phone(request.getPhone())
			.roadAddressName(request.getRoadAddressName())
			.placeUrl(request.getPlaceUrl())
			.relatedUrl(request.getRelateUrl())
			.accommodationInfo(AccommodationInfo.of(request.getInfoTagList()))
			.build();
		this.accommodationDataService.saveAccommodation(accommodation);

		AccommodationReview accommodationReview = AccommodationReview.of(accommodation);
		this.accommodationDataService.saveAccommodationReview(accommodationReview);

		return accommodation;
	}

	@Transactional(readOnly = true)
	public AccommodationsResponse getAccommodations(
		RegionType region,
		AccommodationInfoTag infoTag,
		AccommodationReviewTag reviewTag,
		int pageNumber,
		int pageSize
	) {
		final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id");
		final Page<Accommodation> accommodationPages = accommodationSearchAndFilterRepository.getAccommodationPagesWithFilter(
			region,
			infoTag,
			reviewTag,
			pageable);

		final List<AccommodationDto> accommodationDtos = accommodationPages.getContent().stream()
			.map(AccommodationDto::from)
			.collect(Collectors.toList());

		return AccommodationsResponse.of(accommodationDtos, accommodationPages);
	}

	@Transactional(readOnly = true)
	public AccommodationResponse getAccommodation(long accommodationId, long userId) {
		final Users findUser = userDataService.getUserById(userId);

		final Accommodation findAccommodation = accommodationDataService.getAccommodation(accommodationId);

		final AccommodationDetailDto accommodationDetailDto = AccommodationDetailDto.from(findAccommodation);

		boolean isUserWrite = accommodationDataService.isExistAccommodationReviewAbbreviationHistoryMatchingLatestStatus(findUser, findAccommodation, AccommodationReviewHistoryStatus.WRITE);

		final AccommodationReviewDto accommodationReviewDto = AccommodationReviewDto.from(findAccommodation, isUserWrite);

		return AccommodationResponse.of(
			accommodationDetailDto,
			accommodationReviewDto
		);
	}

	@Transactional
	public void addAccommodationReview(Users user, long accommodationId, AccommodationReviewRequest reviewRequest) {
		final List<AccommodationReviewTag> tagList = reviewRequest.getTags();

		final Users findUser = userDataService.getUserById(user.getId());

		final Accommodation findAccommodation = accommodationDataService.getAccommodation(accommodationId);

		if (findAccommodation.getAccommodationReview() == null) {
			// Check First Accommodation Review;
			AccommodationReview accommodationReview = AccommodationReview.of(findAccommodation);
			accommodationDataService.saveAccommodationReview(accommodationReview);
			findAccommodation.setReview(accommodationReview);
		}

		if (accommodationDataService.isExistAccommodationReviewAbbreviationHistoryMatchingLatestStatus(findUser, findAccommodation, AccommodationReviewHistoryStatus.WRITE)) {
			// Check if User Reviewed This Accommodation
			throw new RuntimeException("");
		}

		// Calculate Cnt
		final AccommodationReview findAccommodationReview = findAccommodation.getAccommodationReview();
		findAccommodationReview.increaseUserCnt();
		findAccommodationReview.addReviews(tagList);
		accommodationDataService.saveAllAccommodationReviewAbbreviation(findAccommodationReview.getCountingInfoList());

		// Create & Save Histories
		AccommodationReviewAbbreviationHistory reviewAbbreviationHistory = AccommodationReviewAbbreviationHistory.of(findUser, findAccommodation, AccommodationReviewHistoryStatus.WRITE, tagList);
		accommodationDataService.saveAccommdoationReviewHistory(reviewAbbreviationHistory);

		accommodationDataService.saveAccommodationReviewHistory(tagList.stream()
			.map(tag -> AccommodationReviewHistory.of(findUser, findAccommodation, AccommodationReviewHistoryStatus.WRITE, tag))
			.collect(Collectors.toList()));
	}

	@Transactional(readOnly = true)
	public AccommodationCurationsResponse getAccommodationCurations() {
		List<Accommodation> accommodations = accommodationDataService.getRandomAccommodation(5);

		List<AccommodationCurationDto> curationDtoList = accommodations.stream()
			.map(AccommodationCurationDto::of)
			.collect(toList());

		return AccommodationCurationsResponse.of(curationDtoList);
	}

//	@Transactional(readOnly = true)
//	public Page<Accommodation> getAccommodationPagesWithFilter(
//		PageRequest pageRequest,
//		RegionType region,
//		AccommodationInfoTag infoTag,
//		AccommodationReviewTag reviewTag
//	) {
//		// TODO: 좀 더 깔끔한 방법 생각해보기
//		if (region != null && infoTag != null) {
//			return accommodationRepository.findAllByRegionAndInfoTag(region.getValue(),
//				infoTag.getName(),
//				pageRequest);
//		}
//
//		if (region == null && infoTag != null) {
//			return accommodationRepository.findAllByInfoTag(infoTag.getName(), pageRequest);
//		}
//
//		if (infoTag == null && region != null) {
//			return accommodationRepository.findAllByRegionType(region, pageRequest);
//		}
//
//		return accommodationRepository.findAll(pageRequest);
//	}

	@Transactional(readOnly = true)
	public List<AccommodationDto> getAccommodationsWithName(String name) {
		return accommodationDataService.getAllByNameContaining(name).stream()
			.map(AccommodationDto::from)
			.collect(Collectors.toList());
	}
}
