package com.workat.domain.accommodation.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.workat.domain.BaseEntity;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.user.entity.Users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccommodationReview extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "accommodation_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Accommodation accommodation;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@Enumerated(EnumType.STRING)
	private AccommodationReviewTag tag;

	private AccommodationReview(AccommodationReviewTag tag,
		Accommodation accommodation,
		Users user
	) {
		this.tag = tag;
		this.accommodation = accommodation;
		this.user = user;
	}

	public static AccommodationReview of(AccommodationReviewTag tag, Accommodation accommodation,
		Users user) {
		return new AccommodationReview(tag, accommodation, user);
	}
}
