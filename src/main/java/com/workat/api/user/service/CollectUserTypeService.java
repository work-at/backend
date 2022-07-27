package com.workat.api.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.user.dto.ActivityTypeDto;
import com.workat.api.user.dto.DepartmentTypeDto;
import com.workat.api.user.dto.DurationTypeDto;
import com.workat.api.user.dto.UserTypeDto;
import com.workat.api.user.dto.response.UserTypeListResponse;
import com.workat.domain.user.activity.ActivityType;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectUserTypeService {

	public UserTypeListResponse collectDepartmentTypes() {
		List<UserTypeDto> list = Arrays.stream(DepartmentType.values())
			.map(jobType -> DepartmentTypeDto.of(jobType.name(), jobType.getType()))
			.collect(Collectors.toList());

		return UserTypeListResponse.of(list);
	}

	public UserTypeListResponse collectDurationTypes() {
		List<UserTypeDto> list = Arrays.stream(DurationType.values())
			.map(jobType -> DurationTypeDto.of(jobType.name(), jobType.getType()))
			.collect(Collectors.toList());

		return UserTypeListResponse.of(list);
	}

	public UserTypeListResponse collectActivityTypes() {
		List<UserTypeDto> list = Arrays.stream(ActivityType.values())
			.map(jobType -> ActivityTypeDto.of(jobType.name(), jobType.getType()))
			.collect(Collectors.toList());

		return UserTypeListResponse.of(list);
	}
}
