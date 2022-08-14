package com.workat.domain.review.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.workat.domain.map.entity.Location;
import com.workat.domain.tag.CafeReviewType;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("C")
@Entity
public class CafeReview extends BaseReview {

	@Enumerated(EnumType.STRING)
	private CafeReviewType reviewType;

	private CafeReview(CafeReviewType reviewType, Location location, Users user) {
		super(location, user);
		this.reviewType = reviewType;
	}

	public static CafeReview of(CafeReviewType reviewType, Location location, Users user) {
		return new CafeReview(reviewType, location, user);
	}

	@Override
	public CafeReviewType getReviewType() {
		return this.reviewType;
	}
}
