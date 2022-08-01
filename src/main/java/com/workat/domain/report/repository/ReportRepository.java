package com.workat.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
