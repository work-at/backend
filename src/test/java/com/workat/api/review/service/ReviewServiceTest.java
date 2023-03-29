package com.workat.api.review.service;

import static com.workat.domain.tag.review.CafeReviewTag.NOT_CROWDED;
import static com.workat.domain.tag.review.CafeReviewTag.PARKING;
import static com.workat.domain.tag.review.CafeReviewTag.VIEW;
import static com.workat.domain.tag.review.CafeReviewTag.WIFI;
import static com.workat.domain.tag.review.RestaurantReviewTag.EAT_ALONE;
import static com.workat.domain.tag.review.RestaurantReviewTag.QUICK_FOOD;
import static com.workat.domain.tag.review.RestaurantReviewTag.SNACK;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewWithUserDto;
import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.common.exception.ConflictException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.DataJpaTestConfig;
import com.workat.domain.review.entity.BaseReview;
import com.workat.domain.review.entity.CafeReview;
import com.workat.domain.review.repository.BaseReviewRepository;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.tag.review.CafeReviewTag;
import com.workat.domain.tag.review.RestaurantReviewTag;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UserProfileRepository;
import com.workat.domain.user.repository.UsersRepository;
import java.util.ArrayList;
import java.util.Arrays;
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

@DisplayName("ReviewService 테스트")
@Import(DataJpaTestConfig.class)
@ActiveProfiles("test")
@DataJpaTest
public class ReviewServiceTest {

	private ReviewService reviewService;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private BaseReviewRepository baseReviewRepository;

	@Autowired
	private LocationRepository locationRepository;

	@BeforeEach
	void setUp() {
		this.reviewService = new ReviewService(usersRepository, baseReviewRepository, locationRepository);
	}

	private List<Location> saveLocations(int size, LocationType category) {
		return IntStream.range(0, size)
			.mapToObj(idx -> {
					String value = String.valueOf(idx);

					KakaoLocalDataDto dto = KakaoLocalDataDto.builder()
						.id(value)
						.phone("000-0000-0000")
						.placeName(value)
						.placeUrl(value)
						.addressName(value)
						.roadAddressName(value)
						.x(value)
						.y(value)
						.build();

					Location location = Location.of(category, dto);

					locationRepository.save(location);

					return location;
				}
			).collect(Collectors.toList());
	}

	private List<Users> saveUsers(int size) {
		return IntStream.range(0, size)
			.mapToObj(idx -> {
					Users user = Users.of(OauthType.KAKAO, (long) idx);
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

	@DisplayName("getLocationReviewsWithUser() 메소드는 리뷰 개수, 타입이 저장한 것과 일치해야 한다")
	@Test
	void reviewTypeSize() {
		// given
		final Location location = saveLocations(1, LocationType.CAFE).get(0);
		final List<Users> users = saveUsers(3);

		final Users user1 = users.get(0);
		final Users user2 = users.get(1);
		final Users user3 = users.get(2);

		// PARKING 2, WIFI 1 저장
		final CafeReview cafeReview1 = CafeReview.of(LocationType.CAFE, user1, location, PARKING);
		final CafeReview cafeReview2 = CafeReview.of(LocationType.CAFE, user2, location, CafeReviewTag.WIFI);
		final CafeReview cafeReview3 = CafeReview.of(LocationType.CAFE, user3, location, PARKING);

		baseReviewRepository.saveAll(Arrays.asList(cafeReview1, cafeReview2, cafeReview3));

		// when
		final long locationId = location.getId();
		final List<ReviewDto> reviewDtos = reviewService.getLocationReviewsWithUser(user1.getId(), locationId, LocationType.CAFE)
			.getReviews();

		final Map<String, Long> reviewCountMap = reviewDtos.stream()
			.collect(
				Collectors.toMap(
					dto -> dto.getReviewType().getName(),
					ReviewDto::getCount));
		// then
		assertAll(
			// PARKING 2, WIFI 1 저장 됐는지 확인
			() -> assertTrue(reviewCountMap.get(PARKING.getName()) == 2),
			() -> assertTrue(reviewCountMap.get(CafeReviewTag.WIFI.getName()) == 1)
		);
	}

	@DisplayName("getLocationReviewsWithUser() 메소드는 location 리뷰들을 count 역순으로 정렬해야 한다")
	@Test
	void getLocationReviewsWithUser_sorted() {
		// given
		final Location location = saveLocations(1, LocationType.CAFE).get(0);
		final List<Users> users = saveUsers(20);

		List<CafeReview> cafeReviews1 = users.subList(0, 10) // 1st 큼
			.stream()
			.map(user -> CafeReview.of(LocationType.CAFE, user, location, PARKING))
			.collect(Collectors.toList());

		List<CafeReview> cafeReviews2 = users.subList(10, 18) // 2nd
			.stream()
			.map(user -> CafeReview.of(LocationType.CAFE, user, location, CafeReviewTag.WIFI))
			.collect(Collectors.toList());

		List<CafeReview> cafeReviews3 = users.subList(18, 20) // 3rd
			.stream()
			.map(user -> CafeReview.of(LocationType.CAFE, user, location, CafeReviewTag.VIEW))
			.collect(Collectors.toList());

		final ArrayList<CafeReview> cafeReviews = new ArrayList<>();
		Stream.of(cafeReviews1, cafeReviews2, cafeReviews3).forEach(cafeReviews::addAll);

		baseReviewRepository.saveAll(cafeReviews);

		// when
		final long locationId = location.getId();
		final Users user = users.get(0);
		final List<ReviewDto> reviewDtos = reviewService.getLocationReviewsWithUser(user.getId(), locationId, LocationType.CAFE)
			.getReviews();

		// then
		assertEquals(reviewDtos.size(), 3); // PARKING, WIFI, VIEW

		for (int i = 0; i < reviewDtos.size() - 1; i++) {
			ReviewDto curDto = reviewDtos.get(i);
			ReviewDto nextDto = reviewDtos.get(i + 1);

			assertTrue(curDto.getCount() >= nextDto.getCount()); // 역순 정렬
		}
	}

	@DisplayName("getLocationReviewsWithUser() 메소드는 user 가 해당 location 에 리뷰를 남겼는지 여부를 알려줘야 한다")
	@Test
	void getLocationReviewsWithUser_userReviewed() {
		// given
		final Location location = saveLocations(1, LocationType.CAFE).get(0);
		final List<Users> users = saveUsers(3);

		final Users user1 = users.get(0);
		final Users user2 = users.get(1);
		final Users user3 = users.get(2);

		final CafeReview cafeReview1 = CafeReview.of(LocationType.CAFE, user1, location, PARKING);
		final CafeReview cafeReview2 = CafeReview.of(LocationType.CAFE, user2, location, CafeReviewTag.WIFI);

		// user1, user2 만 리뷰 저장
		baseReviewRepository.saveAll(Arrays.asList(cafeReview1, cafeReview2));

		// when
		final long locationId = location.getId();

		final ReviewWithUserDto locationReviews1 = reviewService.getLocationReviewsWithUser(user1.getId(), locationId, LocationType.CAFE);
		final ReviewWithUserDto locationReviews2 = reviewService.getLocationReviewsWithUser(user2.getId(), locationId, LocationType.CAFE);
		final ReviewWithUserDto locationReviews3 = reviewService.getLocationReviewsWithUser(user3.getId(), locationId, LocationType.CAFE);

		// then
		assertAll(
			() -> assertTrue(locationReviews1.isUserReviewed()),
			() -> assertTrue(locationReviews2.isUserReviewed()),
			() -> assertFalse(locationReviews3.isUserReviewed()) // user3 은 리뷰 저장 안 됨
		);
	}

	@DisplayName("getLocationReviewsWithUser() 메소드는 리뷰를 남긴 유저의 수를 알려줘야 한다")
	@Test
	void getLocationReviewsWithUser_userCount() {
		// given
		final Location location = saveLocations(1, LocationType.CAFE).get(0);
		final List<Users> users = saveUsers(3);

		final Users user1 = users.get(0);
		final Users user2 = users.get(1);
		final Users user3 = users.get(2);

		final CafeReview cafeReview1ByUser1 = CafeReview.of(LocationType.CAFE, user1, location, PARKING);
		final CafeReview cafeReview2ByUser1 = CafeReview.of(LocationType.CAFE, user1, location, PARKING);
		final CafeReview cafeReview3ByUser1 = CafeReview.of(LocationType.CAFE, user1, location, PARKING);
		final CafeReview cafeReview4ByUser1 = CafeReview.of(LocationType.CAFE, user1, location, PARKING);
		final CafeReview cafeReviewByUser2 = CafeReview.of(LocationType.CAFE, user2, location, CafeReviewTag.WIFI);
		final CafeReview cafeReviewByUser3 = CafeReview.of(LocationType.CAFE, user3, location, CafeReviewTag.WIFI);

		// user1 4개, user2, user3 1개씩 리뷰 저장
		baseReviewRepository.saveAll(Arrays.asList(
			cafeReview1ByUser1,
			cafeReview2ByUser1,
			cafeReview3ByUser1,
			cafeReview4ByUser1,
			cafeReviewByUser2,
			cafeReviewByUser3
		));

		// when
		final long locationId = location.getId();

		final ReviewWithUserDto locationReviews = reviewService.getLocationReviewsWithUser(user1.getId(), locationId, LocationType.CAFE);

		// then
		assertAll(() -> assertEquals(locationReviews.getUserCount(), 6));
	}

	@DisplayName("addCafeReview 메소드는 인자로 주어진 리뷰 타입 개수만큼 리뷰를 저장한다")
	@Test
	void addCafeReview_size() {
		// given
		final Location location = saveLocations(1, LocationType.CAFE).get(0);
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest = ReviewRequest.of(LocationType.CAFE, new ArrayList<>(Arrays.asList(PARKING, WIFI, VIEW)));

		// when
		reviewService.addLocationReview(user.getId(), location.getId(), reviewRequest);

		// then
		List<? extends BaseReview> cafeReviews = baseReviewRepository.findAllByUserAndLocationAndType(user, location, LocationType.CAFE);
		assertEquals(cafeReviews.size(), 3);
	}

	@DisplayName("addCafeReview 메소드는 중복된 유저 id 로 추가하는 경우 실패해야 한다")
	@Test
	void addCafeReview_fail_duplicate_user() {
		// given
		final Location location = saveLocations(1, LocationType.CAFE).get(0);
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest1 = ReviewRequest.of(LocationType.CAFE, new ArrayList<>(List.of(NOT_CROWDED)));
		ReviewRequest reviewRequest2 = ReviewRequest.of(LocationType.CAFE, new ArrayList<>(List.of(EAT_ALONE)));

		// when

		// then
		final long locationId = location.getId();
		assertThrows(ConflictException.class, () -> {
			reviewService.addLocationReview(user.getId(), locationId, reviewRequest1);
			reviewService.addLocationReview(user.getId(), locationId, reviewRequest2);
		});
	}

	@DisplayName("addRestaurantReview 메소드는 인자로 주어진 리뷰 타입 개수만큼 리뷰를 저장한다")
	@Test
	void addRestaurantReview_size() {
		// given
		final Location location = saveLocations(1, LocationType.RESTAURANT).get(0);
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest = ReviewRequest.of(LocationType.RESTAURANT, new ArrayList<>(Arrays.asList(QUICK_FOOD, RestaurantReviewTag.NOT_CROWDED, SNACK)));

		// when
		reviewService.addLocationReview(user.getId(), location.getId(), reviewRequest);

		// then
		List<? extends BaseReview> restaurantReviews = baseReviewRepository.findAllByUserAndLocationAndType(user, location, LocationType.RESTAURANT);

		assertEquals(restaurantReviews.size(), 3);
	}

	@DisplayName("addRestaurantReview 메소드는 중복된 유저 id 로 추가하는 경우 실패해야 한다")
	@Test
	void addRestaurantReview_fail_duplicate_user() {
		// given
		final Location location = saveLocations(1, LocationType.RESTAURANT).get(0);
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest1 = ReviewRequest.of(LocationType.RESTAURANT, List.of(QUICK_FOOD));
		ReviewRequest reviewRequest2 = ReviewRequest.of(LocationType.RESTAURANT, List.of(EAT_ALONE));

		// when

		// then
		final long locationId = location.getId();
		assertThrows(ConflictException.class, () -> {
			reviewService.addLocationReview(user.getId(), locationId, reviewRequest1);
			reviewService.addLocationReview(user.getId(), locationId, reviewRequest2);
		});
	}
}
