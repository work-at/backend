package com.workat.api.report.service;

import org.springframework.stereotype.Service;

import com.workat.api.report.dto.request.ReportRequest;
import com.workat.api.report.dto.response.CSTypeListResponse;
import com.workat.domain.report.CSType;
import com.workat.domain.report.entity.Report;
import com.workat.domain.report.repository.ReportRepository;
import com.workat.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportService {

	private final ReportRepository reportRepository;

	public CSTypeListResponse getReportTypes() {
		return CSTypeListResponse.of(CSType.ALL);
	}

	public void addReport(Users user, ReportRequest request) {
		Report report = Report.of(user, request.getEmail(), request.getType(), request.getContent());
		reportRepository.save(report);
	}
}
