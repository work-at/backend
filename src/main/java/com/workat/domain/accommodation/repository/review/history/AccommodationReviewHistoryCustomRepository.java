package com.workat.domain.accommodation.repository.review.history;

import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReviewHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.user.entity.Users;
import java.util.Optional;

public interface AccommodationReviewHistoryCustomRepository {

	boolean existAccommodationReviewHistoryMatchingStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status);

	Optional<AccommodationReviewHistory> latestAccommodationReviewHistory(Users user, Accommodation accommodation);

	Optional<AccommodationReviewHistory> latestAccommodationReviewHistoryMatchingStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status);
}
