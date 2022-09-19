package com.workat.api.tour.service;

import org.springframework.stereotype.Service;

import com.workat.api.tour.dto.VisitorDegree;
import com.workat.api.tour.dto.response.RegionTrafficResponse;
import com.workat.common.exception.NotFoundException;
import com.workat.domain.accommodation.RegionType;
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

	public RegionTrafficResponse calculateWorkatDegree(RegionType regionCode) {
		MonthlyRegionVisitor monthlyRegionVisitor = monthlyRegionVisitorRepository.findById(regionCode.getCode()).orElseThrow(() -> new NotFoundException(regionCode.name() + " not found"));
		YearlyRegionVisitor yearlyRegionVisitor = yearlyRegionVisitorRepository.findById(regionCode.getCode()).orElseThrow(() -> new NotFoundException(regionCode.name() + " not found"));

		if (yearlyRegionVisitor.getCount() * 0.7 < monthlyRegionVisitor.getCount()) {
			return RegionTrafficResponse.of(regionCode, VisitorDegree.POPULAR);
		}
		if (yearlyRegionVisitor.getCount() * 0.4 > monthlyRegionVisitor.getCount()) {
			return RegionTrafficResponse.of(regionCode, VisitorDegree.FREE);
		}
		return RegionTrafficResponse.of(regionCode, VisitorDegree.IN_BETWEEN);
	}
}
