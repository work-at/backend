package com.workat.domain.accommodation.entity.review;

import com.workat.domain.BaseEntity;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.user.entity.Users;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccommodationReviewHistory extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@JoinColumn(name = "accommodation_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Accommodation accommodation;

	@Enumerated(EnumType.STRING)
	private AccommodationReviewHistoryStatus status;

	public void changeStatus(AccommodationReviewHistoryStatus status) {
		this.status = status;
	}

	public static AccommodationReviewHistory of(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status) {
		return new AccommodationReviewHistory(null, user, accommodation, status);
	}
}
