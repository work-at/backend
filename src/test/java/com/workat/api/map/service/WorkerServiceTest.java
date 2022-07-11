package com.workat.api.map.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.workat.api.map.dto.WorkerDto;
import com.workat.api.map.dto.response.WorkerDetailResponse;
import com.workat.api.map.dto.response.WorkerListResponse;
import com.workat.domain.auth.OauthType;
import com.workat.domain.config.MultipleDatasourceBaseTest;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.repository.UsersRepository;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WorkerServiceTest extends MultipleDatasourceBaseTest {

	private static WorkerLocation workerLocation, workerLocation1, workerLocation2;
	private static Users user;

	@Autowired
	private WorkerLocationRedisRepository workerLocationRedisRepository;
	@Autowired
	private UsersRepository userRepository;
	@Autowired
	private WorkerService workerService;

	@BeforeAll
	void setup() {
		user = Users.builder()
			.nickname("nickname")
			.oauthType(OauthType.KAKAO)
			.oauthId(12345L)
			.position(DepartmentType.ACCOUNTANT)
			.workingYear(DurationType.JUNIOR)
			.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
			.build();
		userRepository.save(user);

		Users user2 = Users.builder()
			.nickname("nick")
			.oauthType(OauthType.KAKAO)
			.oauthId(12346L)
			.position(DepartmentType.ACCOUNTANT)
			.workingYear(DurationType.JUNIOR)
			.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
			.build();
		userRepository.save(user2);

		workerLocation = WorkerLocation.of(user.getId(), String.valueOf(127.423084873712), String.valueOf(37.0789561558879), "경기 안성시 죽산면 죽산리");
		workerLocation1 = WorkerLocation.of(user2.getId(), "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		workerLocation2 = WorkerLocation.of(125L, "127.51", "37.078", "경기 안성시 일죽면 산북리");
		workerLocationRedisRepository.save(workerLocation);
		workerLocationRedisRepository.save(workerLocation1);
		workerLocationRedisRepository.save(workerLocation2);
	}

	@AfterAll
	void teardown() {
		userRepository.deleteAll();
		workerLocationRedisRepository.deleteAll();
	}

	@Test
	void findAllWorkerByLocationNear() {
		//given

		//when
		WorkerListResponse response = workerService.findAllWorkerByLocationNear(user, 5, false);

		//then
		Assertions.assertEquals(response.getResponse().size(), 1);
	}

	@Test
	void findAllWorkerByLocationNearMetaOnly() {
		//given

		//when
		WorkerListResponse response = workerService.findAllWorkerByLocationNear(user, 5, true);

		//then
		Assertions.assertEquals(response.getCount(), 1);
	}

	@Test
	void findWorkerDetailById() {
		//given

		//when
		WorkerDetailResponse response = workerService.findWorkerDetailById(user.getId());

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getId(), user.getId()),
			() -> Assertions.assertEquals(response.getImageUrl(), user.getImageUrl()),
			() -> Assertions.assertEquals(response.getPosition().getName(), user.getPosition().name()),
			() -> Assertions.assertEquals(response.getWorkingYear().getName(), user.getWorkingYear().name()),
			() -> Assertions.assertEquals(response.getStory(), user.getStory())
		);
	}

	@Test
	void findWorkerById() {
		//given

		//when
		WorkerDto response = workerService.findWorkerById(user.getId());

		//then
		Assertions.assertAll(
			() -> Assertions.assertEquals(response.getId(), user.getId()),
			() -> Assertions.assertEquals(response.getImageUrl(), user.getImageUrl()),
			() -> Assertions.assertEquals(response.getPosition().getName(), user.getPosition().name()),
			() -> Assertions.assertEquals(response.getWorkingYear().getName(), user.getWorkingYear().name())
		);
	}
}
