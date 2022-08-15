package com.workat.api.tour.service;

import org.springframework.stereotype.Service;

import com.workat.api.tour.dto.VisitorDegree;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.tour.RegionCode;
import com.workat.domain.tour.entity.MonthlyRegionVisitor;
import com.workat.domain.tour.entity.YearlyRegionVisitor;
import com.workat.domain.tour.repository.MonthlyRegionVisitorRepository;
import com.workat.domain.tour.repository.YearlyRegionVisitorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourApiService {

	private final MonthlyRegionVisitorRepository monthlyRegionVisitorRepository;
	private final YearlyRegionVisitorRepository yearlyRegionVisitorRepository;

	public VisitorDegree calculateWorkatDegree(RegionCode regionCode) { // TODO: 숙소 조회 시 사용
		MonthlyRegionVisitor monthlyRegionVisitor = monthlyRegionVisitorRepository.findById(regionCode.getCode()).orElseThrow(() -> new NotFoundException(regionCode.name() + " not found"));
		YearlyRegionVisitor yearlyRegionVisitor = yearlyRegionVisitorRepository.findById(regionCode.getCode()).orElseThrow(() -> new NotFoundException(regionCode.name() + " not found"));

		if (yearlyRegionVisitor.getCount() < monthlyRegionVisitor.getCount()) {
			return VisitorDegree.POPULAR;
		}
		if (yearlyRegionVisitor.getCount() > monthlyRegionVisitor.getCount()) {
			return VisitorDegree.FREE;
		}
		return VisitorDegree.IN_BETWEEN;
	}
}
