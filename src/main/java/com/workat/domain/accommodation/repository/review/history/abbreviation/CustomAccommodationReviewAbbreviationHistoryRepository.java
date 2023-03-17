package com.workat.domain.accommodation.repository.review.history.abbreviation;

import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviationHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.user.entity.Users;
import java.util.Optional;

public interface CustomAccommodationReviewAbbreviationHistoryRepository {

	boolean isExistAccommodationReviewAbbreviationHistoryMatchingLatestStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status);

	Optional<AccommodationReviewAbbreviationHistory> findLatestAccommodationReviewAbbreviationHistory(Users user, Accommodation accommodation);

	Optional<AccommodationReviewAbbreviationHistory> findLatestAccommodationReviewAbbreviationHistoryMatchingStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status);
}
