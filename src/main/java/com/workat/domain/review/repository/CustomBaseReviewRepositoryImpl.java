package com.workat.domain.review.repository;

import static com.workat.domain.review.entity.QBaseReview.baseReview;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBaseReviewRepositoryImpl implements CustomBaseReviewRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public int getLocationReviewedUserCount(Location location, LocationType type) {
		return jpaQueryFactory.selectFrom(baseReview)
			.where(
				baseReview.location.id.eq(location.getId())
					.and(baseReview.location.type.eq(type))
			).distinct()
			.fetch()
			.size();
	}
}
