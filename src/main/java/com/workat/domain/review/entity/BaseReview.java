package com.workat.domain.review.entity;

import com.workat.domain.BaseEntity;
import com.workat.domain.map.entity.Location;
import com.workat.domain.map.entity.LocationType;
import com.workat.domain.tag.review.ReviewTag;
import com.workat.domain.user.entity.Users;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Entity
public abstract class BaseReview extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	private LocationType type;


	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;


	@JoinColumn(name = "location_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	protected BaseReview(LocationType type, Users user, Location location) {
		this.type = type;
		this.user = user;
		this.location = location;
	}

	public abstract ReviewTag getReviewTag();
}
