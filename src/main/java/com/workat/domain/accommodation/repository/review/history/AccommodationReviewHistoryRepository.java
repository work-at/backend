package com.workat.domain.accommodation.repository.review.history;

import com.workat.domain.accommodation.entity.review.AccommodationReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationReviewHistoryRepository extends JpaRepository<AccommodationReviewHistory, Long>, AccommodationReviewHistoryCustomRepository {
}
