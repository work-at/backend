package com.workat.domain.accommodation.repository.review;

import com.workat.domain.accommodation.entity.review.AccommodationReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
}
