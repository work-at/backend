package com.workat.domain.accommodation.repository.review.history.abbreviation;

import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationReviewAbbreviationHistoryRepository extends JpaRepository<AccommodationReviewAbbreviationHistory, Long>, CustomAccommodationReviewAbbreviationHistoryRepository {
}
