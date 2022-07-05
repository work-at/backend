package com.workat.api.map.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.workat.api.map.dto.WorkerListResponse;
import com.workat.api.user.repository.UserRepository;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.MultipleDatasourceBaseTest;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import com.workat.domain.user.User;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WorkChatServiceTest extends MultipleDatasourceBaseTest {

	@Autowired
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkChatService workChatService;

	private static WorkerLocation workerLocation, workerLocation1, workerLocation2;

	private static User user;

	@BeforeAll
	void setup() {
		user = User.builder()
			.nickname("nickname1")
			.oauthType(OauthType.KAKAO)
			.oauthId(12345)
			.position(DepartmentType.ACCOUNTANT)
			.workingYear(DurationType.JUNIOR)
			.longitude(127.423084873712)
			.latitude(37.0789561558879)
			.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
			.build();
		userRepository.save(user);

		workerLocation = WorkerLocation.of(user.getId(), String.valueOf(user.getLongitude()), String.valueOf(user.getLatitude()), "경기 안성시 죽산면 죽산리");
		workerLocation1 = WorkerLocation.of(124L, "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		workerLocation2 = WorkerLocation.of(125L, "127.51", "37.078", "경기 안성시 일죽면 산북리");
		workerLocationRedisRepository.save(workerLocation);
		workerLocationRedisRepository.save(workerLocation1);
		workerLocationRedisRepository.save(workerLocation2);
	}

	@Test
	void countWorkerByLocationNear() {
		//given

		//when
		int size = workChatService.countWorkerByLocationNear(user.getLongitude(), user.getLatitude(), 5);

		//then
		Assertions.assertEquals(size, 2);
	}

	@Test
	void findAllWorkerByLocationNear() {
		//given

		//when
		WorkerListResponse response = workChatService.findAllWorkerByLocationNear(user.getLongitude(), user.getLatitude(), 5);

		//then
		Assertions.assertEquals(response.getResponse().size(), 1);
	}
}
