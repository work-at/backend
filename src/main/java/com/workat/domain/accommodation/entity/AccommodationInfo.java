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
import com.workat.domain.tag.AccommodationInfoTag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccommodationInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private AccommodationInfoTag tag;

	@JoinColumn(name = "accommodation_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Accommodation accommodation;

	private AccommodationInfo(AccommodationInfoTag tag,
		Accommodation accommodation) {
		this.tag = tag;
		this.accommodation = accommodation;
	}

	public static AccommodationInfo of(AccommodationInfoTag tag,
		Accommodation accommodation) {

		return new AccommodationInfo(tag, accommodation);
	}
}
