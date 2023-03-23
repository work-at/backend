package com.workat.domain.review.entity;

import com.workat.domain.map.entity.LocationType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.workat.domain.map.entity.Location;
import com.workat.domain.tag.review.CafeReviewTag;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "CAFE")
@Entity
public class CafeReview extends BaseReview {

	@Enumerated(EnumType.STRING)
	private CafeReviewTag reviewTag;

	private CafeReview(LocationType category, Users user, Location location, CafeReviewTag reviewTag) {
		super(category, user, location);
		this.reviewTag = reviewTag;
	}

	public static CafeReview of(LocationType category, Users user, Location location, CafeReviewTag reviewTag) {
		return new CafeReview(category, user, location, reviewTag);
	}

	public CafeReviewTag getReviewTag() {
		return this.reviewTag;
	}
}
