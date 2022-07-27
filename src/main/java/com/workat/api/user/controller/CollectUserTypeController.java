package com.workat.api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.user.dto.response.UserTypeListResponse;
import com.workat.api.user.service.CollectUserTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "User JobType Api")
@RequiredArgsConstructor
@RestController
public class CollectUserTypeController {

	private final CollectUserTypeService collectJobTypeService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "직무 목록 조회", notes = "유저 직무 목록을 조회합니다")
	@GetMapping("/api/v1/user/job-department")
	public ResponseEntity<UserTypeListResponse> getJobDepartmentTypes() {
		UserTypeListResponse response = collectJobTypeService.collectDepartmentTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "연차 목록 조회", notes = "유저 연차 목록을 조회합니다")
	@GetMapping("/api/v1/user/job-duration")
	public ResponseEntity<UserTypeListResponse> getJobDurationTypes() {
		UserTypeListResponse response = collectJobTypeService.collectDurationTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "직무 목록 조회", notes = "유저 직무 목록을 조회합니다")
	@GetMapping("/api/v1/user/activities")
	public ResponseEntity<UserTypeListResponse> getActivityTypes() {
		UserTypeListResponse response = collectJobTypeService.collectActivityTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
