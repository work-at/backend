package com.workat.api.map.service;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.workat.api.map.dto.WorkerListResponse;
import com.workat.api.map.dto.WorkerResponse;
import com.workat.api.user.repository.UserRepository;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import com.workat.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkChatService {

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	private final UserRepository userRepository;

	public int countWorkerByLocationNear(double longitude, double latitude, double kilometer) {
		return workerLocationRedisRepository.findAllByLocationNear(new Point(longitude, latitude), new Distance(kilometer, KILOMETERS)).size();
	}

	public WorkerListResponse findAllWorkerByLocationNear(double longitude, double latitude, double kilometer) {
		// TODO: 토큰을 통해 본인 아이디 != WorkerLocation.getUserId() 인 값만 필터링
		List<WorkerResponse> list = workerLocationRedisRepository.findAllByLocationNear(new Point(longitude, latitude), new Distance(kilometer, KILOMETERS))
			.stream()
			.map(workerLocation -> userRepository.findById(workerLocation.getUserId()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(user -> WorkerResponse.of(user.getId(), user.getImageUrl(), user.getPosition(), user.getWorkingYear()))
			.collect(Collectors.toList());

		return WorkerListResponse.of(list);
	}
}
