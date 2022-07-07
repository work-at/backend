package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.WorkerDto;
import com.workat.api.map.dto.response.WorkerDetailResponse;
import com.workat.api.map.service.WorkerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "워케이셔너 api")
@RequiredArgsConstructor
@RestController
public class WorkerController {

	private final WorkerService workerService;

	@GetMapping("/api/v1/map/workers/{userId}/detail")
	public ResponseEntity<WorkerDetailResponse> findWorkerDetailById(@PathVariable("userId") Long userId) {
		WorkerDetailResponse response = workerService.findWorkerDetailById(userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/workers/{userId}")
	public ResponseEntity<WorkerDto> findWorkerById(@PathVariable("userId") Long userId) {
		WorkerDto response = workerService.findWorkerById(userId);
		return ResponseEntity.ok(response);
	}
}
