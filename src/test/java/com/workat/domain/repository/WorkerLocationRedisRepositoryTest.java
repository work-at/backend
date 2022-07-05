package com.workat.domain.repository;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.geo.Distance;
import org.springframework.test.context.ActiveProfiles;

import com.workat.domain.config.RedisContainerBaseTest;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DataRedisTest
class WorkerLocationRedisRepositoryTest extends RedisContainerBaseTest {

	@Autowired
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	private static WorkerLocation workerLocation, workerLocation1, workerLocation2;

	@BeforeAll
	void setup() {
		workerLocation = WorkerLocation.of(123L, "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		workerLocation1 = WorkerLocation.of(124L, "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		workerLocation2 = WorkerLocation.of(125L, "127.51", "37.078", "경기 안성시 일죽면 산북리");
		workerLocationRedisRepository.save(workerLocation);
		workerLocationRedisRepository.save(workerLocation1);
		workerLocationRedisRepository.save(workerLocation2);
	}

	@Test
	void findAllByLocationNear() {
		// given

		// when
		List<WorkerLocation> workerLocations = workerLocationRedisRepository.findAllByLocationNear(workerLocation.getLocation(), new Distance(5, KILOMETERS));

		// then
		Assertions.assertAll(
			() -> Assertions.assertEquals(workerLocations.size(), 2)
		);
	}
}
