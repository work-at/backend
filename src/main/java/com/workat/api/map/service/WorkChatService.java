package com.workat.api.map.service;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkChatService {

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	public int findAllByLocationNear(double longitude, double latitude, double kilometer) {
		return workerLocationRedisRepository.findAllByLocationNear(new Point(longitude, latitude), new Distance(kilometer, KILOMETERS)).size();
	}
}
