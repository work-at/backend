package com.workat.common.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.workat.domain.map.vo.MapRangeInfo;

class DistanceUtilsTest {

	@Test
	void getDistanceKmTest1() {
		double km = DistanceUtils.getDistanceKm(126.977717, 37.566381, 126.978082, 37.565577);
		assertAll(
			() -> assertTrue(km >= 0.094),
			() -> assertTrue(km <= 0.095)
		);
	}

	@Test
	void getDistanceMeterTest1() {
		double meter = DistanceUtils.getDistanceMeter(126.977717, 37.566381, 126.978082, 37.565577);
		assertAll(
			() -> assertTrue(meter >= 94),
			() -> assertTrue(meter <= 95)
		);
	}

	@Test
	void getLocationPointTest() {
		int givenMeter = 2500;
		int resultMeter = givenMeter / 2;
		double fromLongitude = 126.077717;
		double fromLatitude = 37.566381;

		MapRangeInfo mapRangeInfo = DistanceUtils.getLocationPoint(fromLongitude, fromLatitude, givenMeter);

		double minLongDistance = DistanceUtils.getDistanceMeter(fromLongitude, fromLatitude,
			mapRangeInfo.getMinLongitude(), fromLatitude);
		double maxLongDistance = DistanceUtils.getDistanceMeter(fromLongitude, fromLatitude,
			mapRangeInfo.getMaxLongitude(), fromLatitude);
		double minLatDistance = DistanceUtils.getDistanceMeter(fromLongitude, fromLatitude, fromLongitude,
			mapRangeInfo.getMinLatitude());
		double maxLatDistance = DistanceUtils.getDistanceMeter(fromLongitude, fromLatitude, fromLongitude,
			mapRangeInfo.getMaxLatitude());

		assertAll(
			() -> assertTrue(Math.round(minLongDistance) >= resultMeter),
			() -> assertTrue(Math.round(maxLongDistance) >= resultMeter),
			() -> assertTrue(Math.round(minLatDistance) >= resultMeter),
			() -> assertTrue(Math.round(maxLatDistance) >= resultMeter)
		);
	}
}
