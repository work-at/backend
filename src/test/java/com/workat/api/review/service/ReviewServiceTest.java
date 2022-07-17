package com.workat.api.review.service;

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
import org.springframework.test.context.ActiveProfiles;

import com.workat.api.review.dto.LocationReviewDto;
import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.request.ReviewRequest;
import com.workat.common.exception.BadRequestException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.MysqlContainerBaseTest;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.location.LocationRepository;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.review.entity.CafeReview;
import com.workat.domain.review.entity.RestaurantReview;
import com.workat.domain.review.repository.CafeReviewRepository;
import com.workat.domain.review.repository.RestaurantReviewRepository;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UsersRepository;

@DisplayName("ReviewService 테스트")
@ActiveProfiles("test")
@DataJpaTest
public class ReviewServiceTest extends MysqlContainerBaseTest {

	private ReviewService reviewService;

	@Autowired
	private CafeReviewRepository cafeReviewRepository;

	@Autowired
	private RestaurantReviewRepository restaurantReviewRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private UsersRepository userRepository;

	@BeforeEach
	void setUp() {
		this.reviewService = new ReviewService(cafeReviewRepository, restaurantReviewRepository, locationRepository);
	}

	private List<Location> saveLocations(int size, LocationCategory category) {
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
						.longitude(value)
						.latitude(value)
						.build();

					Location location = Location.builder()
						.category(category)
						.dto(dto)
						.build();

					locationRepository.save(location);

					return location;
				}
			).collect(Collectors.toList());
	}

	private List<Users> saveUsers(int size) {
		return IntStream.range(0, size)
			.mapToObj(idx -> {
					Users user = Users.builder()
						.nickname(String.format("name%d", idx))
						.oauthType(OauthType.KAKAO)
						.oauthId(Long.valueOf(idx))
						.position(DepartmentType.ACCOUNTANT)
						.workingYear(DurationType.JUNIOR)
						.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
						.build();

					userRepository.save(user);
					return user;
				}
			).collect(Collectors.toList());
	}

	@DisplayName("getLocationReviews() 메소드는 리뷰 개수, 타입이 저장한 것과 일치해야 한다")
	@Test
	void reviewTypeSize() {
		// given
		final Location location = saveLocations(1, LocationCategory.CAFE).get(0);
		final List<Users> users = saveUsers(3);

		final Users user1 = users.get(0);
		final Users user2 = users.get(1);
		final Users user3 = users.get(2);

		// PARKING 2, WIFI 1 저장
		final CafeReview cafeReview1 = CafeReview.of(CafeReviewType.PARKING, location, user1);
		final CafeReview cafeReview2 = CafeReview.of(CafeReviewType.WIFI, location, user2);
		final CafeReview cafeReview3 = CafeReview.of(CafeReviewType.PARKING, location, user3);

		cafeReviewRepository.saveAll(Arrays.asList(cafeReview1, cafeReview2, cafeReview3));

		// when
		final long locationId = location.getId();
		final List<ReviewDto> reviewDtos = reviewService.getLocationReviews(locationId, user1.getId()).getReviews();

		final Map<String, Long> reviewCountMap = reviewDtos.stream()
			.collect(
				Collectors.toMap(
					dto -> dto.getReviewType().getName(),
					ReviewDto::getCount));
		// then
		assertAll(
			// PARKING 2, WIFI 1 저장 됐는지 확인
			() -> assertTrue(reviewCountMap.get(CafeReviewType.PARKING.getName()) == 2),
			() -> assertTrue(reviewCountMap.get(CafeReviewType.WIFI.getName()) == 1)
		);
	}

	@DisplayName("getLocationReviews() 메소드는 location 리뷰들을 count 역순으로 정렬해야 한다")
	@Test
	void getLocationReviews_sorted() {
		// given
		final Location location = saveLocations(1, LocationCategory.CAFE).get(0);
		final List<Users> users = saveUsers(20);

		List<CafeReview> cafeReviews1 = users.subList(0, 10) // 1st 큼
			.stream()
			.map(user -> CafeReview.of(CafeReviewType.PARKING, location, user))
			.collect(Collectors.toList());

		List<CafeReview> cafeReviews2 = users.subList(10, 18) // 2nd
			.stream()
			.map(user -> CafeReview.of(CafeReviewType.WIFI, location, user))
			.collect(Collectors.toList());

		List<CafeReview> cafeReviews3 = users.subList(18, 20) // 3rd
			.stream()
			.map(user -> CafeReview.of(CafeReviewType.NIGHT_VIEW, location, user))
			.collect(Collectors.toList());

		final ArrayList<CafeReview> cafeReviews = new ArrayList<>();
		Stream.of(cafeReviews1, cafeReviews2, cafeReviews3).forEach(cafeReviews::addAll);

		cafeReviewRepository.saveAll(cafeReviews);

		// when
		final long locationId = location.getId();
		final Users user = users.get(0);
		final List<ReviewDto> reviewDtos = reviewService.getLocationReviews(locationId, user.getId()).getReviews();

		// then
		assertEquals(reviewDtos.size(), 3); // PARKING, WIFI, NIGHT_VIEW

		for (int i = 0; i < reviewDtos.size() - 1; i++) {
			ReviewDto curDto = reviewDtos.get(i);
			ReviewDto nextDto = reviewDtos.get(i + 1);

			assertTrue(curDto.getCount() >= nextDto.getCount()); // 역순 정렬
		}
	}

	@DisplayName("getLocationReviews() 메소드는 user 가 해당 location 에 리뷰를 남겼는지 여부를 알려줘야 한다")
	@Test
	void getLocationReviews_userReviewed() {
		// given
		final Location location = saveLocations(1, LocationCategory.CAFE).get(0);
		final List<Users> users = saveUsers(3);

		final Users user1 = users.get(0);
		final Users user2 = users.get(1);
		final Users user3 = users.get(2);

		final CafeReview cafeReview1 = CafeReview.of(CafeReviewType.PARKING, location, user1);
		final CafeReview cafeReview2 = CafeReview.of(CafeReviewType.WIFI, location, user2);

		// user1, user2 만 리뷰 저장
		cafeReviewRepository.saveAll(Arrays.asList(cafeReview1, cafeReview2));

		// when
		final long locationId = location.getId();

		final LocationReviewDto locationReviews1 = reviewService.getLocationReviews(locationId, user1.getId());
		final LocationReviewDto locationReviews2 = reviewService.getLocationReviews(locationId, user2.getId());
		final LocationReviewDto locationReviews3 = reviewService.getLocationReviews(locationId, user3.getId());

		// then
		assertAll(
			() -> assertTrue(locationReviews1.isUserReviewed()),
			() -> assertTrue(locationReviews2.isUserReviewed()),
			() -> assertFalse(locationReviews3.isUserReviewed()) // user3 은 리뷰 저장 안 됨
		);
	}

	@DisplayName("addCafeReview 메소드는 인자로 주어진 리뷰 타입 개수만큼 리뷰를 저장한다")
	@Test
	void addCafeReview_size() {
		// given
		final Long locationId = saveLocations(1, LocationCategory.CAFE).get(0).getId();
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest = ReviewRequest.of(
			new HashSet<>(Arrays.asList("PARKING", "WIFI", "NIGHT_VIEW")));

		// when
		reviewService.addCafeReview(locationId, reviewRequest, user);

		// then
		final Long userId = user.getId();
		List<CafeReview> cafeReviews = cafeReviewRepository.findAllByLocation_IdAndUser_Id(locationId,
			userId);

		assertEquals(cafeReviews.size(), 3);
	}

	@DisplayName("addCafeReview 메소드는 중복된 유저 id 로 추가하는 경우 실패해야 한다")
	@Test
	void addCafeReview_fail_duplicate_user() {
		// given
		final Location location = saveLocations(1, LocationCategory.CAFE).get(0);
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest1 = ReviewRequest.of(new HashSet<>(Arrays.asList("NOT_CROWDED")));
		ReviewRequest reviewRequest2 = ReviewRequest.of(new HashSet<>(Arrays.asList("EAT_ALONE")));

		// when

		// then
		final long locationId = location.getId();
		assertThrows(BadRequestException.class, () -> {
			reviewService.addCafeReview(locationId, reviewRequest1, user);
			reviewService.addCafeReview(locationId, reviewRequest2, user);
		});
	}

	@DisplayName("addRestaurantReview 메소드는 인자로 주어진 리뷰 타입 개수만큼 리뷰를 저장한다")
	@Test
	void addRestaurantReview_size() {
		// given
		final Long locationId = saveLocations(1, LocationCategory.RESTAURANT).get(0).getId();
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest = ReviewRequest.of(
			new HashSet<>(Arrays.asList("QUICK_FOOD", "NOT_CROWDED", "SNACK")));

		// when
		reviewService.addRestaurantReview(locationId, reviewRequest, user);

		// then
		final Long userId = user.getId();
		List<RestaurantReview> restaurantReviews = restaurantReviewRepository.findAllByLocation_IdAndUser_Id(locationId,
			userId);

		assertEquals(restaurantReviews.size(), 3);
	}

	@DisplayName("addRestaurantReview 메소드는 중복된 유저 id 로 추가하는 경우 실패해야 한다")
	@Test
	void addRestaurantReview_fail_duplicate_user() {
		// given
		final Location location = saveLocations(1, LocationCategory.RESTAURANT).get(0);
		final Users user = saveUsers(1).get(0);

		ReviewRequest reviewRequest1 = ReviewRequest.of(new HashSet<>(Arrays.asList("QUICK_FOOD")));
		ReviewRequest reviewRequest2 = ReviewRequest.of(new HashSet<>(Arrays.asList("EAT_ALONE")));

		// when

		// then
		final long locationId = location.getId();
		assertThrows(BadRequestException.class, () -> {
			reviewService.addRestaurantReview(locationId, reviewRequest1, user);
			reviewService.addRestaurantReview(locationId, reviewRequest2, user);
		});
	}
}
