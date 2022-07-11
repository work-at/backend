package com.workat.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.review.entity.CafeReview;

public interface CafeReviewRepository extends JpaRepository<CafeReview, Long> {
	List<CafeReview> findAllByLocation_Id(long locationId);

	Optional<CafeReview> findByLocation_IdAndUser_Id(long locationId, long userId);

	List<CafeReview> findAllByLocation_IdAndUser_Id(long locationId, long userId);
}
