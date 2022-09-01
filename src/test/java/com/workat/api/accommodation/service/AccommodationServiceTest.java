package com.workat.api.accommodation.service;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationDto;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.AccommodationInfo;
import com.workat.domain.accommodation.entity.AccommodationReview;
import com.workat.domain.accommodation.repository.AccommodationInfoRepository;
import com.workat.domain.accommodation.repository.AccommodationRepository;
import com.workat.domain.accommodation.repository.AccommodationReviewRepository;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.DataJpaTestConfig;
import com.workat.domain.config.MysqlContainerBaseTest;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.tag.dto.TagDto;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UserProfileRepository;

@DisplayName("AccommodationService 테스트")
@Import(DataJpaTestConfig.class)
@ActiveProfiles("test")
@DataJpaTest
public class AccommodationServiceTest extends MysqlContainerBaseTest {

	private AccommodationService accommodationService;

	@Autowired
	private AccommodationRepository accommodationRepository;

	@Autowired
	private AccommodationInfoRepository accommodationInfoRepository;

	@Autowired
	private AccommodationReviewRepository accommodationReviewRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@BeforeEach
	void setUp() {
		this.accommodationService = new AccommodationService(accommodationRepository,
			accommodationReviewRepository,
			accommodationInfoRepository);

	}

	@DisplayName("getAccommodation 메소드는 accommodationId 에 매핑된 accommodation entity 에 해당하는 데이터를 반환해야 한다")
	@Test
	void getAccommodationTest() {
		// given
		Accommodation accommodation = saveAccommodations(1).get(0);
		List<AccommodationInfo> accommodationInfos = accommodationInfoRepository.findAllByAccommodation(
			accommodation);
		HashSet<TagDto> accommodationInfoDtos = accommodationInfos.stream()
			.map(AccommodationInfo::getTag)
			.map(TagDto::of)
			.collect(toCollection(HashSet::new));
		Users user = saveUsers(1).get(0);

		// when
		AccommodationDetailDto given = accommodationService.getAccommodation(accommodation.getId(),
			user.getId()).getAccommodationDetail();

		HashSet<TagDto> givenInfoTags = given.getInfoTags();

		// then
		assertAll(
			() -> assertEquals(given.getId(), accommodation.getId()),
			() -> assertEquals(given.getName(), accommodation.getName()),
			() -> assertEquals(given.getImgUrl(), accommodation.getImgUrl()),
			() -> assertEquals(given.getPrice(), accommodation.getPrice()),
			() -> assertEquals(given.getPhone(), accommodation.getPhone()),
			() -> assertEquals(given.getRoadAddressName(), accommodation.getRoadAddressName()),
			() -> assertEquals(given.getPlaceUrl(), accommodation.getPlaceUrl()),
			() -> assertEquals(given.getRelatedUrl(), accommodation.getRelatedUrl()),
			() -> assertEquals(
				accommodationInfoDtos.size(),
				givenInfoTags.size())
		);
	}

	@DisplayName("getAccommodation 메소드는 태그별 리뷰 개수를 repository 에 저장된대로 반환해야 한다")
	@Test
	void getAccommodation_review() {
		// given
		Accommodation accommodation = saveAccommodations(1).get(0);
		List<Users> users = saveUsers(3);

		Users user1 = users.get(0);
		Users user2 = users.get(1);
		Users user3 = users.get(2);

		// FOCUS 2, POWER 1 저장
		final AccommodationReview accommodationReview1 = AccommodationReview.of(AccommodationReviewTag.FOCUS,
			accommodation, user1);
		final AccommodationReview accommodationReview2 = AccommodationReview.of(AccommodationReviewTag.FOCUS,
			accommodation, user2);
		final AccommodationReview accommodationReview3 = AccommodationReview.of(AccommodationReviewTag.POWER,
			accommodation, user3);

		accommodationReviewRepository.saveAll(
			Arrays.asList(accommodationReview1, accommodationReview2, accommodationReview3));

		// when
		List<TagCountDto> tagCountDtos = accommodationService.getAccommodation(accommodation.getId(),
				user1.getId())
			.getAccommodationReview()
			.getReviews();

		Map<String, Long> countMap = tagCountDtos.stream()
			.collect(Collectors.toMap(
				dto -> dto.getTag().getName(),
				TagCountDto::getCount));

		// then
		assertAll(
			() -> assertEquals(countMap.get(AccommodationReviewTag.FOCUS.getName()), 2),
			() -> assertEquals(countMap.get(AccommodationReviewTag.POWER.getName()), 1)
		);
	}

	@DisplayName("getAccommodation 메소드는 리뷰들을 count 역순으로 정렬해야 한다")
	@Test
	void getAccommodation_review_sort() {
		// given
		Accommodation accommodation = saveAccommodations(1).get(0);
		List<Users> users = saveUsers(20);

		List<AccommodationReview> reviewPart1 = users.subList(0, 10) // 1st 가장 큼
			.stream()
			.map(user -> AccommodationReview.of(AccommodationReviewTag.FOCUS, accommodation, user))
			.collect(Collectors.toList());

		List<AccommodationReview> reviewPart2 = users.subList(10, 18) // 2nd
			.stream()
			.map(user -> AccommodationReview.of(AccommodationReviewTag.SERVE_MEAL, accommodation, user))
			.collect(Collectors.toList());

		List<AccommodationReview> reviewPart3 = users.subList(18, 20) // 3rd
			.stream()
			.map(user -> AccommodationReview.of(AccommodationReviewTag.POWER, accommodation, user))
			.collect(Collectors.toList());

		final ArrayList<AccommodationReview> reviews = new ArrayList<>();
		Stream.of(reviewPart1, reviewPart2, reviewPart3).forEach(reviews::addAll);

		accommodationReviewRepository.saveAll(reviews);

		// when
		Long accommodationId = accommodation.getId();
		Long userId = users.get(0).getId();

		List<TagCountDto> tagCountDtos = accommodationService.getAccommodation(accommodationId, userId)
			.getAccommodationReview()
			.getReviews();

		// then
		assertEquals(tagCountDtos.size(), 3); // FOCUS, SERVE_MEAL, POWER

		for (int i = 0; i < tagCountDtos.size() - 1; i++) {
			TagCountDto curDto = tagCountDtos.get(i);
			TagCountDto nextDto = tagCountDtos.get(i + 1);

			assertTrue(curDto.getCount() >= nextDto.getCount()); // 역순 정렬
		}
	}

	@DisplayName("getAccommodations 메소드는 accommodation entity 에 해당하는 데이터를 반환해야 한다")
	@Test
	void getAccommodations() {
		// given
		List<Accommodation> accommodations = saveAccommodations(3);

		// when
		List<AccommodationDto> accommodationDtos = accommodationService.getAccommodations(RegionType.SEOUL, null, null,
			0,
			10).getAccommodations();

		// then
		assertEquals(accommodations.size(), accommodationDtos.size());
	}

	private List<Users> saveUsers(int size) {
		return IntStream.range(0, size)
			.mapToObj(idx -> {
					Users user = Users.of(OauthType.KAKAO, (long)idx);
					UserProfile userProfile = UserProfile.builder()
						.user(user)
						.nickname(String.format("name%d", idx))
						.position(DepartmentType.ENGINEER)
						.workingYear(DurationType.JUNIOR)
						.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
						.build();
					userProfileRepository.save(userProfile);
					return user;
				}
			).collect(Collectors.toList());
	}

	private List<Accommodation> saveAccommodations(int size) {

		List<Accommodation> accommodations = IntStream.range(0, size)
			.mapToObj(idx -> {
					String mockValue = String.valueOf(idx);

					Accommodation accommodation = Accommodation.builder()
						.regionType(RegionType.SEOUL)
						.name("name" + mockValue)
						.imgUrl("imgUrl" + mockValue)
						.price(Long.valueOf(mockValue))
						.phone(mockValue)
						.roadAddressName("roadAddressName" + mockValue)
						.placeUrl("placeUrl" + mockValue)
						.relatedUrl("relatedUrl" + mockValue)
						.build();

					List<AccommodationInfo> accommodationInfos = Arrays.asList(AccommodationInfoTag.NEAR_CITY,
							AccommodationInfoTag.WORKSPACE,
							AccommodationInfoTag.SHARED_WORKSPACE
						).stream()
						.map(tag ->
							AccommodationInfo.of(AccommodationInfoTag.NEAR_CITY, accommodation)
						).collect(Collectors.toList());

					accommodationInfoRepository.saveAll(accommodationInfos);

					return accommodation;
				}
			).collect(Collectors.toList());

		accommodationRepository.saveAll(accommodations);

		return accommodations;
	}

}
