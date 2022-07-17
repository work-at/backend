package com.workat.domain.review.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.workat.domain.map.entity.Location;
import com.workat.domain.review.FoodReviewType;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("R")
@Entity
public class RestaurantReview extends BaseReview {

	@Enumerated(EnumType.STRING)
	private FoodReviewType reviewType;

	private RestaurantReview(FoodReviewType reviewType, Location location, Users user) {
		super(location, user);
		this.reviewType = reviewType;
	}

	static public RestaurantReview of(FoodReviewType reviewType, Location location, Users user) {
		return new RestaurantReview(reviewType, location, user);
	}

	@Override
	public FoodReviewType getReviewType() {
		return this.reviewType;
	}
}

