package com.workat.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.review.entity.RestaurantReview;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
	List<RestaurantReview> findAllByLocation_Id(long locationId);

	Optional<RestaurantReview> findByLocation_IdAndUser_Id(long locationId, long userId);

	List<RestaurantReview> findAllByLocation_IdAndUser_Id(long locationId, long userId);
}
