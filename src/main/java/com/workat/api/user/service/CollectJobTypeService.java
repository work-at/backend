package com.workat.api.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.user.dto.DepartmentTypeResponse;
import com.workat.api.user.dto.JobTypeListResponse;
import com.workat.api.user.dto.DurationTypeResponse;
import com.workat.api.user.dto.JobTypeResponse;
import com.workat.domain.user.job.DurationType;
import com.workat.domain.user.job.DepartmentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectJobTypeService {

	public JobTypeListResponse collectDepartmentTypes() {
		List<JobTypeResponse> list = Arrays.stream(DepartmentType.values())
			.map(jobType -> DepartmentTypeResponse.of(jobType.name(), jobType.getType()))
			.collect(Collectors.toList());

		return JobTypeListResponse.of(list);
	}

	public JobTypeListResponse collectDurationTypes() {
		List<JobTypeResponse> list = Arrays.stream(DurationType.values())
			.map(jobType -> DurationTypeResponse.of(jobType.name(), jobType.getType(), jobType.getDescription()))
			.collect(Collectors.toList());

		return JobTypeListResponse.of(list);
	}
}
