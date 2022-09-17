package com.workat.domain.locationReview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.workat.domain.locationReview.entity.CafeReview;

public interface CafeReviewRepository extends JpaRepository<CafeReview, Long> {
	List<CafeReview> findAllByLocation_Id(long locationId);

	Optional<CafeReview> findByLocation_IdAndUser_Id(long locationId, long userId);

	List<CafeReview> findAllByLocation_IdAndUser_Id(long locationId, long userId);

	@Query(value = "SELECT COUNT(DISTINCT review.user.id) FROM CafeReview AS review WHERE review.location.id = :locationId")
	Integer countDistinctUserByLocationId(long locationId);
}
