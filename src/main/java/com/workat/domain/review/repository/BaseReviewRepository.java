package com.workat.domain.review.repository;

import com.workat.domain.review.entity.BaseReview;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;
import com.workat.domain.user.entity.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseReviewRepository extends JpaRepository<BaseReview, Long>, CustomBaseReviewRepository {

	List<? extends BaseReview> findAllByTypeAndLocation(LocationType type, Location location);

	List<? extends BaseReview> findAllByUserAndLocationAndType(Users user, Location location, LocationType type);
}
