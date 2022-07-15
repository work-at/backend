package com.workat.domain.map.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MapRangeInfo {

	private double fromLongitude;

	private double fromLatitude;

	private double minLongitude;

	private double maxLongitude;

	private double minLatitude;

	private double maxLatitude;
}
