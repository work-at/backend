package com.workat.api.map.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.workat.api.map.dto.WorkerListResponse;
import com.workat.api.user.repository.UserRepository;
import com.workat.domain.auth.OauthType;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import com.workat.domain.user.User;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

@ExtendWith(MockitoExtension.class)
public class WorkChatServiceTest {

	@Mock
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private WorkChatService workChatService;

	@Test
	void countWorkerByLocationNear() {
		//given
		WorkerLocation workerLocation1 = WorkerLocation.of("123", "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		WorkerLocation workerLocation2 = WorkerLocation.of("124", "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		Mockito.when(workerLocationRedisRepository.findAllByLocationNear(any(), any())).thenReturn(List.of(workerLocation1, workerLocation2));

		//when
		int size = workChatService.countWorkerByLocationNear(127.423084873712, 37.0789561558879, 5);

		//then
		Assertions.assertEquals(size, 2);
	}

	@Test
	void findAllWorkerByLocationNear() {
		//given
		User user1 = User.builder()
			.id(UUID.randomUUID())
			.nickname("nickname1")
			.oauthType(OauthType.KAKAO)
			.oauthId(12345)
			.position(DepartmentType.ACCOUNTANT)
			.workingYear(DurationType.JUNIOR)
			.longitude(127.423084873712)
			.latitude(37.0789561558879)
			.imageUrl("https://avatars.githubusercontent.com/u/46469385?v=4")
			.build();

		UUID userId2 = UUID.randomUUID();

		Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
		Mockito.when(userRepository.findById(userId2.toString())).thenReturn(Optional.empty());

		WorkerLocation workerLocation1 = WorkerLocation.of(user1.getId(), "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		WorkerLocation workerLocation2 = WorkerLocation.of(userId2.toString(), "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		Mockito.when(workerLocationRedisRepository.findAllByLocationNear(any(), any())).thenReturn(List.of(workerLocation1, workerLocation2));

		//when
		WorkerListResponse response = workChatService.findAllWorkerByLocationNear(127.423084873712, 37.0789561558879, 5);

		//then
		Assertions.assertEquals(response.getResponse().size(), 1);
	}
}
