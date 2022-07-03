package com.workat.api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.user.dto.JobTypeListResponse;
import com.workat.api.user.service.CollectJobTypeService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class CollectJobTypeController {

	private final CollectJobTypeService collectJobTypeService;

	@GetMapping("/user/job-department")
	public ResponseEntity<JobTypeListResponse> getJobDepartmentTypes() {
		JobTypeListResponse response = collectJobTypeService.collectDepartmentTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/user/job-duration")
	public ResponseEntity<JobTypeListResponse> getJobDurationTypes() {
		JobTypeListResponse response = collectJobTypeService.collectDurationTypes();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
