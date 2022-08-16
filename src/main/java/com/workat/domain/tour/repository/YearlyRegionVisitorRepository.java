package com.workat.domain.tour.repository;

import org.springframework.data.repository.CrudRepository;

import com.workat.domain.tour.entity.MonthlyRegionVisitor;
import com.workat.domain.tour.entity.YearlyRegionVisitor;

public interface YearlyRegionVisitorRepository extends CrudRepository<YearlyRegionVisitor, String> {
}
