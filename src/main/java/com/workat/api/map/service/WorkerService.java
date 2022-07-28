package com.workat.api.map.service;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workat.api.map.dto.WorkerDto;
import com.workat.api.map.dto.WorkerPinDto;
import com.workat.api.map.dto.response.WorkerListResponse;
import com.workat.api.map.dto.response.WorkerPinResponse;
import com.workat.api.map.dto.response.WorkerSizeResponse;
import com.workat.api.user.dto.ActivityTypeDto;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.chat.repository.room.ChatRoomCustomRepository;
import com.workat.domain.chat.repository.room.ChatRoomRepository;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.repository.worker.WorkerLocationRedisRepository;
import com.workat.domain.user.entity.UserActivity;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.entity.Users;
import com.workat.domain.user.repository.UserActivityRepository;
import com.workat.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkerService {

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	private final UserProfileRepository userProfileRepository;

	private final ChatRoomRepository chatRoomRepository;

	private final UserActivityRepository userActivityRepository;

	@Transactional(readOnly = true)
	public WorkerListResponse findAllWorkerByLocationNear(Users user, double kilometer) {
		List<WorkerLocation> workerLocations = getWorkerByLocationNear(user, kilometer);

		List<WorkerDto> workerDtos = workerLocations
			.stream()
			.filter(workerLocation -> user.getId() != Long.parseLong(workerLocation.getUserId()))
			.map(workerLocation -> userProfileRepository.findById(Long.parseLong(workerLocation.getUserId())))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(userProfile -> {
				int workchats = chatRoomRepository.findAllByUser(userProfile.getUser()).size();
				List<ActivityTypeDto> activityTypes = userActivityRepository.findByUser_Id(userProfile.getId()).stream()
					.map(UserActivity::getActivity)
					.map(activity -> ActivityTypeDto.of(activity.name(), activity.getType()))
					.collect(Collectors.toList());
				return WorkerDto.of(userProfile, workchats, activityTypes);
			})
			.collect(Collectors.toList());

		return WorkerListResponse.of(workerDtos);
	}

	@Transactional(readOnly = true)
	public WorkerPinResponse findAllWorkerPinsByLocationNear(Users user, double kilometer) {
		WorkerLocation userLocation = workerLocationRedisRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("user need to update address"));

		List<WorkerPinDto> workerPinDtos = workerLocationRedisRepository.findWorkerPinsByLocationNear(userLocation.getLocation(), kilometer)
			.stream()
			.filter(workerPin -> !user.getId().equals(workerPin.getUserId()))
			.map(WorkerPinDto::of)
			.collect(Collectors.toList());

		return WorkerPinResponse.of(workerPinDtos);
	}

	@Transactional(readOnly = true)
	public WorkerSizeResponse countAllWorkerByLocationNear(Users user, double kilometer) {
		List<WorkerLocation> workerLocations = getWorkerByLocationNear(user, kilometer);

		return WorkerSizeResponse.of(Math.max(workerLocations.size() - 1, 0));
	}

	@Transactional(readOnly = true)
	public WorkerDto findWorkerById(Long userId) {
		UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> new NotFoundException("워케이셔너가 존재하지 않습니다"));
		int workchats = chatRoomRepository.findAllByUser(userProfile.getUser()).size();
		List<ActivityTypeDto> activityTypes = userActivityRepository.findByUser_Id(userProfile.getId()).stream()
			.map(UserActivity::getActivity)
			.map(activity -> ActivityTypeDto.of(activity.name(), activity.getType()))
			.collect(Collectors.toList());
		return WorkerDto.of(userProfile, workchats, activityTypes);
	}

	private List<WorkerLocation> getWorkerByLocationNear(Users user, double kilometer) {
		WorkerLocation userLocation = workerLocationRedisRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("user need to update address"));

		return workerLocationRedisRepository.findAllByLocationNear(userLocation.getLocation(), new Distance(kilometer, KILOMETERS));
	}
}
