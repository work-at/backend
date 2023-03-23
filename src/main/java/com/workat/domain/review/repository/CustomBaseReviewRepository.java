package com.workat.domain.review.repository;

import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;

public interface CustomBaseReviewRepository {

	int getLocationReviewedUserCount(Location location, LocationType type);
}
