package com.workat.api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.user.dto.response.JobTypeListResponse;
import com.workat.api.user.service.CollectJobTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "직무 및 연차 타입 조회")
@RequiredArgsConstructor
@RestController
public class CollectJobTypeController {

	private final CollectJobTypeService collectJobTypeService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "직무 목록 조회", notes = "유저 직무 목록을 조회합니다")
	@GetMapping("/api/v1/user/job-department")
	public ResponseEntity<JobTypeListResponse> getJobDepartmentTypes() {
		JobTypeListResponse response = collectJobTypeService.collectDepartmentTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "연차 목록 조회", notes = "유저 연차 목록을 조회합니다")
	@GetMapping("/api/v1/user/job-duration")
	public ResponseEntity<JobTypeListResponse> getJobDurationTypes() {
		JobTypeListResponse response = collectJobTypeService.collectDurationTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
