package com.workat.domain.map.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.workat.domain.map.http.dto.KakaoLocalDataDto;

import lombok.Builder;
import lombok.Getter;

// 나중에 사용할 예정 현재는 주석으로 처리해둠
// @Table(uniqueConstraints = {@UniqueConstraint(name = "location_unique", columnNames = "placeId")})
@Getter
@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private LocationCategory category;

	@Column(unique = true)
	private String placeId;

	@Column
	private String phone;

	@Column
	private String placeName;

	@Column
	private String placeUrl;

	@Column
	private String addressName;

	@Column
	private String roadAddressName;

	@Column
	private double longitude;

	@Column
	private double latitude;

	protected Location() {

	}

	@Builder
	public Location(LocationCategory category, KakaoLocalDataDto dto) {
		this.category = category;
		this.phone = dto.getPhone();
		this.placeId = dto.getId();
		this.placeName = dto.getPlaceName();
		this.placeUrl = dto.getPlaceUrl();
		this.addressName = dto.getAddressName();
		this.roadAddressName = dto.getRoadAddressName();
		this.longitude = Float.parseFloat(dto.getLongitude());
		this.latitude = Float.parseFloat(dto.getLatitude());
	}

	public Location update(KakaoLocalDataDto dto) {
		this.phone = dto.getPhone();
		this.placeName = dto.getPlaceName();
		this.placeUrl = dto.getPlaceUrl();
		this.addressName = dto.getAddressName();
		this.roadAddressName = dto.getRoadAddressName();
		this.longitude = Double.parseDouble(dto.getLongitude());
		this.latitude = Double.parseDouble(dto.getLatitude());
		return this;
	}
}
