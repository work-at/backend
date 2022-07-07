package com.workat.api.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.user.dto.DepartmentTypeDto;
import com.workat.api.user.dto.DurationTypeDto;
import com.workat.api.user.dto.JobTypeDto;
import com.workat.api.user.dto.response.JobTypeListResponse;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectJobTypeService {

	public JobTypeListResponse collectDepartmentTypes() {
		List<JobTypeDto> list = Arrays.stream(DepartmentType.values())
			.map(jobType -> DepartmentTypeDto.of(jobType.name(), jobType.getType()))
			.collect(Collectors.toList());

		return JobTypeListResponse.of(list);
	}

	public JobTypeListResponse collectDurationTypes() {
		List<JobTypeDto> list = Arrays.stream(DurationType.values())
			.map(jobType -> DurationTypeDto.of(jobType.name(), jobType.getType()))
			.collect(Collectors.toList());

		return JobTypeListResponse.of(list);
	}
}
