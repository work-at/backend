package com.workat.domain.map.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.workat.domain.BaseEntity;
import com.workat.domain.map.http.dto.KakaoLocalDataDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 나중에 사용할 예정 현재는 주석으로 처리해둠
// @Table(uniqueConstraints = {@UniqueConstraint(name = "location_unique", columnNames = "placeId")})
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Location extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private LocationType type;

	@Column(unique = true)
	private String placeId;

	@Column
	private String phone;

	@Column
	private String placeName;

	@Column
	private String thumbnailImageUrl;

	@Column
	String fullImageUrl;

	@Column
	private String placeUrl;

	@Column
	private String placeCategory;

	@Column
	private String addressName;

	@Column
	private String roadAddressName;

	@Column
	private double longitude;

	@Column
	private double latitude;

	public static Location of(LocationType type, KakaoLocalDataDto dto) {
		return Location.builder()
			.type(type)
			.phone(dto.getPhone())
			.placeId(dto.getId())
			.placeName(dto.getPlaceName())
			.placeUrl(dto.getPlaceUrl())
			.placeCategory(dto.getCategoryName())
			.thumbnailImageUrl(dto.getThumbnailImageUrl())
			.fullImageUrl(dto.getFullImageUrl())
			.addressName(dto.getAddressName())
			.roadAddressName(dto.getRoadAddressName())
			.longitude(Double.parseDouble(dto.getX()))
			.latitude(Double.parseDouble(dto.getY()))
			.build();
	}

	public Location update(KakaoLocalDataDto dto) {
		this.phone = dto.getPhone();
		this.placeName = dto.getPlaceName();
		this.placeUrl = dto.getPlaceUrl();
		this.addressName = dto.getAddressName();
		this.roadAddressName = dto.getRoadAddressName();
		this.longitude = Double.parseDouble(dto.getX());
		this.latitude = Double.parseDouble(dto.getY());
		return this;
	}

	public Location setImages(String[] images) {
		this.thumbnailImageUrl = images[0];
		this.fullImageUrl = images[1];
		return this;
	}
}
