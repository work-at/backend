package com.workat.domain.review.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.workat.domain.map.entity.Location;
import com.workat.domain.review.CafeReviewType;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CafeReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "location_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@Enumerated(EnumType.STRING)
	private CafeReviewType reviewType;

	private CafeReview(CafeReviewType reviewType, Location location, Users user) {
		this.reviewType = reviewType;
		this.location = location;
		this.user = user;
	}

	static public CafeReview of(CafeReviewType reviewType, Location location, Users user) {
		return new CafeReview(reviewType, location, user);
	}
}
