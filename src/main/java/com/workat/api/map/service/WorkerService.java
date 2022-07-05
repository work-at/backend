package com.workat.api.map.service;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.workat.api.map.dto.WorkerDto;
import com.workat.api.map.dto.response.WorkerDetailResponse;
import com.workat.api.map.dto.response.WorkerListResponse;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkerService {

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	private final UserRepository userRepository;

	public int countWorkerByLocationNear(double longitude, double latitude, double kilometer) {
		return workerLocationRedisRepository.findAllByLocationNear(new Point(longitude, latitude),
			new Distance(kilometer, KILOMETERS)).size();
	}

	public WorkerListResponse findAllWorkerByLocationNear(double longitude, double latitude, double kilometer) {
		// TODO: 토큰을 통해 본인 아이디 != WorkerLocation.getUserId() 인 값만 필터링
		List<WorkerDto> list = workerLocationRedisRepository.findAllByLocationNear(new Point(longitude, latitude),
				new Distance(kilometer, KILOMETERS))
			.stream()
			.map(workerLocation -> userRepository.findById(workerLocation.getUserId()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(user -> WorkerDto.of(user.getId(), user.getImageUrl(), user.getPosition(), user.getWorkingYear()))
			.collect(Collectors.toList());

		return WorkerListResponse.of(list);
	}

	public WorkerDetailResponse findWorkerDetailById(Long userId) {
		Users user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		return WorkerDetailResponse.of(user.getId(), user.getImageUrl(), user.getPosition(), user.getWorkingYear(),
			user.getStory());
	}

	public WorkerDto findWorkerById(Long userId) {
		Users user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		return WorkerDto.of(user.getId(), user.getImageUrl(), user.getPosition(), user.getWorkingYear());
	}
}
