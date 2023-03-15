package com.workat.domain.accommodation.repository.review.history;

import static com.workat.domain.accommodation.entity.QAccommodationReviewHistory.accommodationReviewHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.review.AccommodationReviewHistory;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.user.entity.Users;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AccommodationReviewHistoryCustomRepositoryImpl implements AccommodationReviewHistoryCustomRepository {


	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public boolean existAccommodationReviewHistoryMatchingStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		AccommodationReviewHistory history = jpaQueryFactory
			.selectFrom(accommodationReviewHistory)
			.where(accommodationReviewHistory.user.id.eq(user.getId())
				.and(accommodationReviewHistory.accommodation.id.eq(accommodation.getId())))
			.orderBy(accommodationReviewHistory.createdDate.desc())
			.stream().findFirst().orElse(null);

		if (history == null) {
			return false;
		} else {
			return history.getStatus().equals(status);
		}
	}

	@Override
	public Optional<AccommodationReviewHistory> latestAccommodationReviewHistory(Users user, Accommodation accommodation) {
		return Optional.empty();
	}

	@Override
	public Optional<AccommodationReviewHistory> latestAccommodationReviewHistoryMatchingStatus(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		return Optional.empty();
	}

}
