package com.workat.domain.review.entity;

import com.workat.domain.map.entity.LocationType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.workat.domain.map.entity.Location;
import com.workat.domain.tag.review.RestaurantReviewTag;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "RESTAURANT")
@Entity
public class RestaurantReview extends BaseReview {

	@Enumerated(EnumType.STRING)
	private RestaurantReviewTag reviewTag;

	private RestaurantReview(LocationType category, Users user, Location location, RestaurantReviewTag reviewTag) {
		super(category, user, location);
		this.reviewTag = reviewTag;
	}

	public static RestaurantReview of(LocationType category, Users user, Location location, RestaurantReviewTag reviewTag) {
		return new RestaurantReview(category, user, location, reviewTag);
	}

	@Override
	public RestaurantReviewTag getReviewTag() {
		return this.reviewTag;
	}
}

