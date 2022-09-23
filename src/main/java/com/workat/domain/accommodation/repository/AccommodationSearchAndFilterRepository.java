package com.workat.domain.accommodation.repository;

import static com.workat.domain.accommodation.entity.QAccommodation.*;
import static com.workat.domain.accommodation.entity.QAccommodationInfo.*;
import static com.workat.domain.accommodation.entity.QAccommodationReview.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.tag.AccommodationInfoTag;
import com.workat.domain.tag.AccommodationReviewTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccommodationSearchAndFilterRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<Accommodation> getAccommodationPagesWithFilter(
		RegionType region,
		AccommodationInfoTag infoTag,
		AccommodationReviewTag reviewTag,
		Pageable pageable
	) {

		List<Accommodation> content = jpaQueryFactory
			.select(accommodation)
			.from(accommodation)
			.leftJoin(accommodationReview).on(accommodation.id.eq(accommodationReview.accommodation.id))
			.leftJoin(accommodationInfo).on(accommodation.id.eq(accommodationInfo.accommodation.id))
			.where(
				isRegion(region),
				isFilteredInfo(infoTag),
				isFilteredReview(reviewTag)
			)
			.orderBy(accommodation.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(content, pageable, totalCount(region, infoTag, reviewTag));
	}

	private long totalCount(
		RegionType region,
		AccommodationInfoTag infoTag,
		AccommodationReviewTag reviewTag
	) {
		Long count = jpaQueryFactory
			.select(accommodation.count())
			.from(accommodation)
			.leftJoin(accommodationReview).on(accommodation.id.eq(accommodationReview.accommodation.id))
			.leftJoin(accommodationInfo).on(accommodation.id.eq(accommodationInfo.accommodation.id))
			.where(
				isRegion(region),
				isFilteredInfo(infoTag),
				isFilteredReview(reviewTag)
			)
			.fetchOne();
		if (count == null) {
			return 0L;
		}
		return count;
	}

	private BooleanExpression isRegion(RegionType region) {
		if (region == null) {
			return null;
		}
		return accommodation.regionType.eq(region);
	}

	private BooleanExpression isFilteredInfo(AccommodationInfoTag infoTag) {
		if (infoTag == null) {
			return null;
		}
		return accommodationInfo.tag.eq(infoTag);
	}

	private BooleanExpression isFilteredReview(AccommodationReviewTag reviewTag) {
		if (reviewTag == null) {
			return null;
		}
		return accommodationReview.tag.eq(reviewTag);
	}
}
