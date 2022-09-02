package com.workat.domain.accommodation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workat.domain.accommodation.entity.AccommodationReview;
import com.workat.domain.tag.AccommodationReviewTag;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {

	List<AccommodationReview> findAllByAccommodation_Id(Long accommodationId);

	Long countByTag(AccommodationReviewTag tag);

	List<AccommodationReview> findAllByAccommodation_IdAndUser_Id(Long accommodationId, Long userId);

}
