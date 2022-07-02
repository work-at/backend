package com.workat.domain.map.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.workat.domain.map.http.dto.KakaoLocalDataDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 나중에 사용할 예정 현재는 주석으로 처리해둠
// @Table(uniqueConstraints = {@UniqueConstraint(name = "location_unique", columnNames = "placeId")})
@Getter
@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private LocationCategory category;

	@Column
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
	private double x;

	@Column
	private double y;

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
		this.x = Float.parseFloat(dto.getX());
		this.y = Float.parseFloat(dto.getY());
	}
}
