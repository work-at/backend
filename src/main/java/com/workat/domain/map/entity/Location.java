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

@Table(uniqueConstraints = {@UniqueConstraint(name = "location_unique", columnNames = "placeId")})
@Getter
@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String placeId;

	@Enumerated(EnumType.STRING)
	private LocationCategory category;

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
	private float x;

	@Column
	private float y;

	protected Location() {

	}

	@Builder
	public Location(LocationCategory category, KakaoLocalDataDto dto) {
		Location location = new Location();
		location.category = category;
		location.phone = dto.getPhone();
		location.placeId = dto.getId();
		location.placeName = dto.getPlaceName();
		location.placeUrl = dto.getPlaceUrl();
		location.addressName = dto.getAddressName();
		location.roadAddressName = dto.getRoadAddressName();
		location.x = Float.parseFloat(dto.getX());
		location.y = Float.parseFloat(dto.getY());
	}
}
