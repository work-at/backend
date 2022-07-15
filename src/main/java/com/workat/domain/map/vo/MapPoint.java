package com.workat.domain.map.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MapPoint {

	private double longitude;

	private double latitude;

	public static MapPoint of(double longitude, double latitude) {
	    return new MapPoint(longitude, latitude);
	}
}
