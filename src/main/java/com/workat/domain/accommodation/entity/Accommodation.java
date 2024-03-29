package com.workat.domain.accommodation.entity;

import com.workat.api.accommodation.dto.CsvAccommodationDto;
import com.workat.domain.BaseEntity;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.embed.AccommodationInfo;
import com.workat.domain.accommodation.entity.review.AccommodationReview;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Accommodation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private RegionType regionType;

	@Column(name = "accommodation_name")
	private String name;

	@Column
	private String imgUrl;

	@Column
	private String thumbnailImgUrl;

	@Column
	private Long price;

	@Column
	private String phone;

	@Column
	private String roadAddressName;

	@Column
	private String placeUrl;

	@Column
	private String relatedUrl;

	@Embedded
	private AccommodationInfo accommodationInfo;

	@OneToOne(mappedBy = "accommodation")
	private AccommodationReview accommodationReview;

	@Builder
	public Accommodation(RegionType regionType,
		String name,
		String thumbnailImgUrl,
		String imgUrl,
		Long price,
		String phone,
		String roadAddressName,
		String placeUrl,
		String relatedUrl,
		AccommodationInfo accommodationInfo
	) {
		this.regionType = regionType;
		this.name = name;
		this.imgUrl = imgUrl;
		this.thumbnailImgUrl = thumbnailImgUrl;
		this.price = price;
		this.phone = phone;
		this.roadAddressName = roadAddressName;
		this.placeUrl = placeUrl;
		this.relatedUrl = relatedUrl;
		this.accommodationInfo = accommodationInfo;
	}

	public void setReview(AccommodationReview review) {
		this.accommodationReview = review;
	}

	public void update(CsvAccommodationDto dto) {
		this.regionType = dto.getRegionType();
		this.name = dto.getName();
		this.thumbnailImgUrl = dto.getThumbnailImgUrl();
		this.imgUrl = dto.getImgUrl();
		this.roadAddressName = dto.getRoadAddress();
		this.phone = dto.getPhone();
		this.price = Long.parseLong(dto.getPrice().replaceAll("[^0-9]", ""));
		this.relatedUrl = dto.getRelatedUrl();
		this.placeUrl = dto.getPlaceUrl();
	}

	public void update(AccommodationReview review) {
		this.accommodationReview = review;
	}
}
