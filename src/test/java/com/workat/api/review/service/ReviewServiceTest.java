package com.workat.api.review.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.workat.api.review.dto.CafeReviewDto;
import com.workat.api.review.dto.request.CafeReviewRequest;
import com.workat.common.exception.BadRequestException;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.MysqlContainerBaseTest;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationCategory;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;
import com.workat.domain.map.repository.LocationRepository;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.review.entity.CafeReview;
import com.workat.domain.review.repository.CafeReviewRepository;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ReviewServiceTest extends MysqlContainerBaseTest {

	private ReviewService reviewService;

	@Autowired
	private CafeReviewRepository cafeReviewRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private UsersRepository userRepository;

	@BeforeEach
	void setUp() {
		this.reviewService = new ReviewService(cafeReviewRepository, locationRepository);
	}

	private List<Location> saveLocations(int size) {
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

								Location location = Location.builder()
															.category(LocationCategory.CAFE)
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

	@Test
	void getCafeReviews() { // cafe 리뷰들 조회
		// given
		final Location location = saveLocations(1).get(0);
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
		final List<CafeReviewDto> cafeReviewDtos = reviewService.getCafeReviews(locationId)
																.getCafeReviewDtos();

		final Map<String, Long> reviewCountMap = cafeReviewDtos.stream()
															   .collect(
																   Collectors.toMap(
																	   dto -> dto.getReviewTypeDto().getName(),
																	   CafeReviewDto::getCount));
		// then
		assertAll(
			// PARKING 2, WIFI 1 저장 됐는지 확인
			() -> assertTrue(reviewCountMap.get(CafeReviewType.PARKING.getName()) == 2),
			() -> assertTrue(reviewCountMap.get(CafeReviewType.WIFI.getName()) == 1)
		);
	}

	@Test
	void getCafeReviews_sorted() { // cafe 리뷰들은 정렬되어야 한다
		// given
		final Location location = saveLocations(1).get(0);
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
		final List<CafeReviewDto> cafeReviewDtos = reviewService.getCafeReviews(locationId)
																.getCafeReviewDtos();

		// then
		assertEquals(cafeReviewDtos.size(), 3); // PARKING, WIFI, NIGHT_VIEW

		for (int i = 0; i < cafeReviewDtos.size() - 1; i++) {
			CafeReviewDto curDto = cafeReviewDtos.get(i);
			CafeReviewDto nextDto = cafeReviewDtos.get(i + 1);

			assertTrue(curDto.getCount() >= nextDto.getCount()); // 역순 정렬
		}

	}

	@Test
	void addCafeReview() { // cafe 리뷰 추가
		// given
		final Long locationId = saveLocations(1).get(0).getId();
		final Users user = saveUsers(1).get(0);

		CafeReviewRequest reviewRequest = CafeReviewRequest.of(Arrays.asList("PARKING", "WIFI", "NIGHT_VIEW"));

		// when
		reviewService.addCafeReview(locationId, reviewRequest, user);

		// then
		final Long userId = user.getId();
		List<CafeReview> allByLocation_idAndUser_id = cafeReviewRepository.findAllByLocation_IdAndUser_Id(locationId,
			userId);

		assertTrue(allByLocation_idAndUser_id.size() == 3);
	}

	@Test
	void addCafeReview_fail_duplicate_user() { // 중복된 유저 id 로 cafe 리뷰 추가한 경우 실패
		// given
		final Location location = saveLocations(1).get(0);
		final Users user = saveUsers(1).get(0);

		CafeReviewRequest reviewRequest1 = CafeReviewRequest.of(Arrays.asList("PARKING"));
		CafeReviewRequest reviewRequest2 = CafeReviewRequest.of(Arrays.asList("WIFI"));

		// when

		// then
		final long locationId = location.getId();
		assertThrows(BadRequestException.class, () -> {
			reviewService.addCafeReview(locationId, reviewRequest1, user);
			reviewService.addCafeReview(locationId, reviewRequest2, user);
		});
	}
}
