package com.workat.domain.accommodation.entity.review;

import com.workat.domain.BaseEntity;
import com.workat.domain.tag.AccommodationReviewTag;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AccommodationReviewCounting extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Enumerated(value = EnumType.STRING)
	private AccommodationReviewTag category;

	private int cnt;

	public void increaseCnt() {
		this.cnt++;
	}

	public void decreaseCnt() {
		this.cnt--;
	}

	public static AccommodationReviewCounting of(AccommodationReviewTag tag) {
		return new AccommodationReviewCounting(null, tag, 0);
	}

	@Override
	public int hashCode() {
		return category.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AccommodationReviewCounting) {
			return this.category.equals(((AccommodationReviewCounting) obj).category);
		} else {
			return false;
		}
	}
}
