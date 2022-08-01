package com.workat.api.report.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.report.dto.request.ReportRequest;
import com.workat.api.report.service.ReportService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "Report Api")
@RequiredArgsConstructor
@RestController
public class ReportController {

	private final ReportService reportService;

	@PostMapping("/api/v1/reports")
	public ResponseEntity<?> addReport(@UserValidation Users user, @Valid @RequestBody ReportRequest request) {
		reportService.addReport(user, request);
		return ResponseEntity.ok().build();
	}
}
