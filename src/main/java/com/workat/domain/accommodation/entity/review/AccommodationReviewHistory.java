package com.workat.domain.accommodation.entity.review;

import com.workat.domain.BaseEntity;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.tag.AccommodationReviewTag;
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

/*
 * Basic History
 * Select를 하기에는 비효율적이나 Base가 되는 Hisotry를 쌓는다.
 * 이유는 추후 어떤식으로 서비스가 확장될지 모르기때문에 History를 남긴다.
 * 단순히 데이터 전달을 위한 것은 AccommodationAbbreviation.class을 비롯한 클래스로 관리한다.
 * 따라서 단순 Insert하고 쌓기만한다.
 */
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

	private AccommodationReviewHistoryStatus status;

	@Enumerated(value = EnumType.STRING)
	private AccommodationReviewTag tag;

	public static AccommodationReviewHistory of(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status, AccommodationReviewTag tag) {
		return new AccommodationReviewHistory(null, user, accommodation, status, tag);
	}
}
