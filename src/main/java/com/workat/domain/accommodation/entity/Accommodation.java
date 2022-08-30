package com.workat.domain.accommodation.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.workat.domain.accommodation.RegionType;
import com.workat.domain.tag.AccommodationInfoTag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Accommodation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private RegionType regionType;

	@Column
	private String name;

	@Column
	private String imgUrl;

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

	@OneToMany(mappedBy = "accommodation")
	private List<AccommodationInfo> infos;

	@Builder
	public Accommodation(RegionType regionType,
		String name,
		String imgUrl,
		Long price,
		String phone,
		String roadAddressName,
		String placeUrl, String relatedUrl,
		List<AccommodationInfo> infos
	) {
		this.regionType = regionType;
		this.name = name;
		this.imgUrl = imgUrl;
		this.price = price;
		this.phone = phone;
		this.roadAddressName = roadAddressName;
		this.placeUrl = placeUrl;
		this.relatedUrl = relatedUrl;
		this.infos = infos;
	}

	public List<AccommodationInfoTag> getInfoTags() {
		return infos.stream()
			.map(AccommodationInfo::getTag)
			.collect(Collectors.toList());
	}
}
