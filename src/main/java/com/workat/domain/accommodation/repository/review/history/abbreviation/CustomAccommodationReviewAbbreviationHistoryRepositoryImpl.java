package com.workat.domain.accommodation.repository.review.history.abbreviation;


import static com.workat.domain.accommodation.entity.review.abbreviation.QAccommodationReviewAbbreviationHistory.accommodationReviewAbbreviationHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.abbreviation.AccommodationReviewAbbreviationHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.user.entity.Users;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomAccommodationReviewAbbreviationHistoryRepositoryImpl implements CustomAccommodationReviewAbbreviationHistoryRepository {


	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public boolean isExistAccommodationReviewAbbreviationHistoryMatchingLatestStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		AccommodationReviewAbbreviationHistory history = jpaQueryFactory
			.selectFrom(accommodationReviewAbbreviationHistory)
			.where(accommodationReviewAbbreviationHistory.user.id.eq(user.getId())
				.and(accommodationReviewAbbreviationHistory.accommodation.id.eq(accommodation.getId())))
			.orderBy(accommodationReviewAbbreviationHistory.createdDate.desc())
			.stream().findFirst().orElse(null);

		if (history == null) {
			return false;
		} else {
			return history.getStatus().equals(status);
		}
	}

	@Override
	public Optional<AccommodationReviewAbbreviationHistory> findLatestAccommodationReviewAbbreviationHistory(Users user, Accommodation accommodation) {
		return Optional.empty();
	}

	@Override
	public Optional<AccommodationReviewAbbreviationHistory> findLatestAccommodationReviewAbbreviationHistoryMatchingStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		return Optional.empty();
	}

}
