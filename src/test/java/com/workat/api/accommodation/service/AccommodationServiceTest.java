package com.workat.api.accommodation.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.api.accommodation.dto.AccommodationDetailDto;
import com.workat.api.accommodation.dto.AccommodationDto;
import com.workat.api.accommodation.dto.request.AccommodationReviewRequest;
import com.workat.api.accommodation.service.data.AccommodationDataService;
import com.workat.api.user.service.data.UserDataService;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.embed.AccommodationInfo;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReview;
import com.workat.domain.accommodation.repository.AccommodationRepository;
import com.workat.domain.accommodation.repository.AccommodationSearchAndFilterRepository;
import com.workat.domain.accommodation.repository.review.AccommodationReviewRepository;
import com.workat.domain.accommodation.repository.review.counting.AccommodationReviewCountingRepository;
import com.workat.domain.accommodation.repository.review.history.AccommodationReviewHistoryRepository;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.DataJpaTestConfig;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.dto.TagCountDto;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("AccommodationService 테스트")
@ActiveProfiles("test")
@Import({DataJpaTestConfig.class, MockitoExtension.class})
@DataJpaTest
public class AccommodationServiceTest {

	@Autowired
	private EntityManager em;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private AccommodationService accommodationService;

	private UserDataService userDataService;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	private AccommodationDataService accommodationDataService;

	@Autowired
	private AccommodationRepository accommodationRepository;

	@Autowired
	private AccommodationReviewRepository accommodationReviewRepository;

	@Autowired
	private AccommodationReviewCountingRepository accommodationReviewCountingRepository;

	@Autowired
	private AccommodationReviewHistoryRepository accommodationReviewHistoryRepository;

	//TODO Merge
	private AccommodationSearchAndFilterRepository accommodationSearchAndFilterRepository;

	@BeforeEach
	void setUp() {
		this.userDataService = new UserDataService(usersRepository, userProfileRepository);
		this.accommodationDataService = new AccommodationDataService(accommodationRepository, accommodationReviewRepository, accommodationReviewCountingRepository, accommodationReviewHistoryRepository);
		this.accommodationSearchAndFilterRepository = new AccommodationSearchAndFilterRepository(jpaQueryFactory);
		this.accommodationService = new AccommodationService(userDataService, accommodationDataService, accommodationSearchAndFilterRepository);
	}

	@AfterEach
	void tearDown() {
		this.accommodationRepository.deleteAll();
		this.accommodationReviewHistoryRepository.deleteAll();
	}

	@DisplayName("getAccommodation 메소드는 accommodationId 에 매핑된 accommodation entity 에 해당하는 데이터를 반환해야 한다")
	@Test
	void getAccommodationTest() {
		// given
		Users user = saveUsers(1).get(0);
		Accommodation accommodation = saveAccommodations(1).get(0);

		// when
		AccommodationDetailDto given = accommodationService.getAccommodation(accommodation.getId(), user.getId()).getAccommodationDetail();

		// then
		assertAll(
			() -> assertEquals(given.getId(), accommodation.getId()),
			() -> assertEquals(given.getName(), accommodation.getName()),
			() -> assertEquals(given.getImgUrl(), "/uploaded" + accommodation.getImgUrl() + ".png"),
			() -> assertEquals(given.getPrice(), accommodation.getPrice()),
			() -> assertEquals(given.getPhone(), accommodation.getPhone()),
			() -> assertEquals(given.getRoadAddressName(), accommodation.getRoadAddressName()),
			() -> assertEquals(given.getPlaceUrl(), accommodation.getPlaceUrl()),
			() -> assertEquals(given.getRelatedUrl(), accommodation.getRelatedUrl()),
			() -> assertEquals(3, accommodation.getAccommodationInfo().getTags().size())
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
		em.flush();

		// FOCUS 2, POWER 1 저장
		accommodationService.addAccommodationReview(user1, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.LOCATION)));
		accommodationService.addAccommodationReview(user2, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.LOCATION)));
		accommodationService.addAccommodationReview(user3, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.POWER)));

		// when
		List<TagCountDto> tagCountDtos = accommodationService.getAccommodation(accommodation.getId(), user1.getId())
			.getAccommodationReview()
			.getReviews();

		Map<String, Long> countMap = tagCountDtos.stream()
			.collect(Collectors.toMap(dto ->
				dto.getTag().getName(), TagCountDto::getCount));

		// then
		assertAll(
			() -> assertEquals(countMap.get(AccommodationReviewTag.LOCATION.getName()), 2),
			() -> assertEquals(countMap.get(AccommodationReviewTag.POWER.getName()), 1)
		);
	}

	@DisplayName("getAccommodation 메소드는 리뷰들을 count 역순으로 정렬해야 한다")
	@Test
	void getAccommodation_review_sort() {
		// given
		Accommodation accommodation = saveAccommodations(1).get(0);
		AccommodationReview accommodationReview = AccommodationReview.of(accommodation);
		accommodation.setReview(accommodationReview);
		accommodationReviewRepository.save(accommodationReview);
		List<Users> users = saveUsers(20);

		users.subList(0, 10) // 1st 가장 큼
			.forEach(user -> accommodationService.addAccommodationReview(user, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.LOCATION))));
		users.subList(10, 18) // 2st 가장 큼
			.forEach(user -> accommodationService.addAccommodationReview(user, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.BREAKFAST))));
		users.subList(18, 20) // 3st 가장 큼
			.forEach(user -> accommodationService.addAccommodationReview(user, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.POWER))));

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
		accommodations.forEach(accommodation -> {
			AccommodationReview review = AccommodationReview.of(accommodation);
			accommodation.setReview(review);
			accommodationReviewRepository.save(review);
		});

		// when
		List<AccommodationDto> accommodationDtos = accommodationService.getAccommodations(RegionType.SEOUL, null,
			null, 0, 10).getAccommodations();

		// then
		assertEquals(accommodations.size(), accommodationDtos.size());
	}

	@DisplayName("getAccommodations 메소드는 상위 count 에 해당하는 리뷰 태그들을 필드로 반환해야 한다")
	@Test
	void getAccommodations_top_reviews() {
		// given
		Accommodation accommodation = saveAccommodations(1).get(0);
		AccommodationReview accommodationReview = AccommodationReview.of(accommodation);
		accommodation.setReview(accommodationReview);
		accommodationReviewRepository.save(accommodationReview);
		List<Users> users = saveUsers(20);

		users.subList(0, 10) // 1st 가장 큼
			.forEach(user -> accommodationService.addAccommodationReview(user, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.LOCATION))));
		users.subList(10, 18) // 2st 가장 큼
			.forEach(user -> accommodationService.addAccommodationReview(user, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.BREAKFAST))));
		users.subList(18, 20) // 3st 가장 큼
			.forEach(user -> accommodationService.addAccommodationReview(user, accommodation.getId(), AccommodationReviewRequest.of(List.of(AccommodationReviewTag.POWER))));

		List<AccommodationDto> accommodationDtos = accommodationService.getAccommodations(RegionType.SEOUL, null,
			null, 0, 10).getAccommodations();

		// then
		accommodationDtos.stream()
			.forEach(dto -> {
				List<AccommodationReviewTag> tags = dto.getTopReviewTags().stream()
					.map(tagDto -> AccommodationReviewTag.of(tagDto.getName()))
					.collect(Collectors.toList());

				assertAll(
					() -> assertTrue(tags.contains(AccommodationReviewTag.LOCATION)),
					() -> assertTrue(tags.contains(AccommodationReviewTag.BREAKFAST)),
					() -> assertTrue(tags.contains(AccommodationReviewTag.POWER))
				);
			});
	}

	private List<Users> saveUsers(int size) {
		List<Users> users = IntStream.range(0, size)
			.mapToObj(idx -> {
					Users user = Users.of(OauthType.KAKAO, (long) idx);
					UserProfile userProfile = UserProfile.builder()
						.user(user)
						.nickname(String.format("name%d", idx))
						.position(DepartmentType.ENGINEER)
						.workingYear(DurationType.JUNIOR)
						.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
						.build();
					userDataService.saveProfile(userProfile);
					return user;
				}
			).collect(Collectors.toList());
		return users;
	}

	private List<Accommodation> saveAccommodations(int size) {
		List<Accommodation> accommodations = IntStream.range(0, size)
			.mapToObj(idx -> {
					String mockValue = String.valueOf(idx);
					AccommodationInfo accommodationInfo = AccommodationInfo.of(List.of(
						AccommodationInfoTag.NEAR_CITY,
						AccommodationInfoTag.WORKSPACE,
						AccommodationInfoTag.SHARED_WORKSPACE));

					return Accommodation.builder()
						.regionType(RegionType.SEOUL)
						.name("name" + mockValue)
						.imgUrl("imgUrl" + mockValue)
						.price(Long.valueOf(mockValue))
						.phone(mockValue)
						.roadAddressName("roadAddressName" + mockValue)
						.placeUrl("placeUrl" + mockValue)
						.relatedUrl("relatedUrl" + mockValue)
						.accommodationInfo(accommodationInfo)
						.build();
				}
			).collect(Collectors.toList());
		return accommodationRepository.saveAll(accommodations);
	}
}
