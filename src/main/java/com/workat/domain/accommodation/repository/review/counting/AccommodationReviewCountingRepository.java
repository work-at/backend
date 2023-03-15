package com.workat.domain.accommodation.repository.review.counting;

import com.workat.domain.accommodation.entity.review.AccommodationReviewCounting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationReviewCountingRepository extends JpaRepository<AccommodationReviewCounting, Long> {
}
