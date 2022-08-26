package com.workat.domain.accommodation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

	@Column
	private String name;

	@Column
	private String imgUrl;

	@Column
	private long price;

	@Column
	private String phone;

	@Column
	private String roadAddressName;

	@Column
	private String placeUrl;

	@Column
	private String relatedUrl;

	@Builder
	public Accommodation(String name, String imgUrl, long price, String phone, String roadAddressName,
		String placeUrl, String relatedUrl) {
		this.name = name;
		this.imgUrl = imgUrl;
		this.price = price;
		this.phone = phone;
		this.roadAddressName = roadAddressName;
		this.placeUrl = placeUrl;
		this.relatedUrl = relatedUrl;
	}
}
