package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.WorkerDetailResponse;
import com.workat.api.map.dto.WorkerResponse;
import com.workat.api.map.service.WorkChatService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class WorkChatController {

	private final WorkChatService workChatService;

	@GetMapping("/map/workers/{userId}/detail")
	public ResponseEntity<WorkerDetailResponse> findWorkerDetailById(@PathVariable("userId") Long userId) {
		WorkerDetailResponse response = workChatService.findWorkerDetailById(userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/map/workers/{userId}")
	public ResponseEntity<WorkerResponse> findWorkerById(@PathVariable("userId") Long userId) {
		WorkerResponse response = workChatService.findWorkerById(userId);
		return ResponseEntity.ok(response);
	}
}
