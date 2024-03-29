package com.workat.api.map.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.WorkerDto;
import com.workat.api.map.dto.response.WorkerListResponse;
import com.workat.api.map.dto.response.WorkerPinResponse;
import com.workat.api.map.dto.response.WorkerSizeResponse;
import com.workat.api.map.service.WorkerService;
import com.workat.common.annotation.UserValidation;
import com.workat.common.util.UrlUtils;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "Map Workationer Api")
@RequiredArgsConstructor
@RestController
public class WorkerController {

	private final WorkerService workerService;

	@GetMapping("/api/v1/map/workers/{userId}")
	public ResponseEntity<WorkerDto> findWorkerById(@PathVariable("userId") Long userId, HttpServletRequest request) {
		String baseUrl = UrlUtils.getBaseUrl(request);
		WorkerDto response = workerService.findWorkerById(userId, baseUrl);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/workers")
	public ResponseEntity<WorkerListResponse> findNearWorker(
		@UserValidation Users user,
		@RequestParam(defaultValue = "5.0") double kilometer,
		HttpServletRequest request) {
		String baseUrl = UrlUtils.getBaseUrl(request);
		WorkerListResponse response = workerService.findAllWorkerByLocationNear(user, kilometer, baseUrl);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/workers/pins")
	public ResponseEntity<WorkerPinResponse> findNearWorkerPins(@UserValidation Users user, @RequestParam(defaultValue = "5.0") double kilometer) {
		WorkerPinResponse response = workerService.findAllWorkerPinsByLocationNear(user, kilometer);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/v1/map/workers/counting")
	public ResponseEntity<WorkerSizeResponse> countNearWorker(@UserValidation Users user, @RequestParam(defaultValue = "5.0") double kilometer) {
		WorkerSizeResponse response = workerService.countAllWorkerByLocationNear(user, kilometer);
		return ResponseEntity.ok(response);
	}
}
