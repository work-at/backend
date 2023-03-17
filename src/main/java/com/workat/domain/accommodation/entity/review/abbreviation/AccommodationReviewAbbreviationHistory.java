package com.workat.domain.accommodation.entity.review.abbreviation;

import com.workat.domain.BaseEntity;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.enums.AccommodationReviewHistoryStatus;
import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.user.entity.Users;
import java.util.List;
import java.util.stream.Collectors;
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
public class AccommodationReviewAbbreviationHistory extends BaseEntity {

	private static final String DELIMITER = "||";

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Users user;

	@JoinColumn(name = "accommodation_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Accommodation accommodation;

	// 이것은 조회를 위한 것으로 WRITE만 상태를 기록하고 DELETE인 경우는 row를 삭제한다.
	@Enumerated(EnumType.STRING)
	private AccommodationReviewHistoryStatus status;

	private String tags;

	public static AccommodationReviewAbbreviationHistory of(Users user, Accommodation accommodation, AccommodationReviewHistoryStatus status, List<AccommodationReviewTag> tagList) {
		List<String> tagStringList = tagList.stream()
			.map(AccommodationReviewTag::getName)
			.collect(Collectors.toList());
		String tagStr = String.join(DELIMITER, tagStringList);
		return new AccommodationReviewAbbreviationHistory(null, user, accommodation, status, tagStr);
	}
}
