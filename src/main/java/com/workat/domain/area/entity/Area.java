package com.workat.domain.area.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Area {

	@Id
	private String name;

	private String address;

	private double longitude;

	private double latitude;

	public static Area of(String name, String address, double longitude, double latitude) {
		return new Area(name, address, longitude, latitude);
	}
}
