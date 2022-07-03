package com.workat.domain.repository;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.test.context.ActiveProfiles;

import com.workat.domain.config.RedisContainerBaseTest;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;

@SpringBootTest
@ActiveProfiles("test")
class WorkerLocationRedisRepositoryTest extends RedisContainerBaseTest {

	@Autowired
	private WorkerLocationRedisRepository workerLocationRedisRepository;

	@Test
	void findAllByLocationNear() {
		// given
		WorkerLocation workerLocation1 = WorkerLocation.of("123", "127.423084873712", "37.0789561558879", "경기 안성시 죽산면 죽산리");
		WorkerLocation workerLocation2 = WorkerLocation.of("124", "127.40", "37.07895", "경기 안성시 삼죽면 내장리");
		WorkerLocation workerLocation3 = WorkerLocation.of("125", "127.51", "37.078", "경기 안성시 일죽면 산북리");
		workerLocationRedisRepository.save(workerLocation1);
		workerLocationRedisRepository.save(workerLocation2);
		workerLocationRedisRepository.save(workerLocation3);

		// when
		List<WorkerLocation> workerLocations = workerLocationRedisRepository.findAllByLocationNear(workerLocation1.getLocation(), new Distance(5, KILOMETERS));

		// then
		Assertions.assertAll(
			() -> Assertions.assertEquals(workerLocations.size(), 2)
		);
	}
}
