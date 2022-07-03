package com.workat.api.map.service;

import static org.mockito.ArgumentMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;

@ExtendWith(MockitoExtension.class)
public class WorkChatServiceTest {

	@Mock
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	@InjectMocks
	private WorkChatService workChatService;

	@Test
	void testFindAllByLocationNear() {
		//given
		WorkerLocation workerLocation1 = WorkerLocation.of("123", "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		WorkerLocation workerLocation2 = WorkerLocation.of("124", "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		Mockito.when(workerLocationRedisRepository.findAllByLocationNear(any(), any())).thenReturn(List.of(workerLocation1, workerLocation2));

		//when
		int size = workChatService.findAllByLocationNear(127.423084873712, 37.0789561558879, 5);

		//then
		Assertions.assertEquals(size, 2);
	}
}
