package com.workat.common.util;

import com.workat.domain.map.vo.MapRangeInfo;

public class DistanceUtils {

	private static final double LONG_DEGREE_CONST = 88.9036;

	private static final double LONG_MINUTE_CONST = 1.4817;

	private static final double LONG_SECOND_CONST = 0.0246;

	private static final double LAT_DEGREE_CONST = 111.3194;

	private static final double LAT_MINUTE_CONST = 1.8553;

	private static final double LAT_SECOND_CONST = 0.0309;

	public static double getDistanceKm(double fromLongitude, double fromLatitude, double toLongitude,
		double toLatitude) {
		return getDistance(fromLongitude, fromLatitude, toLongitude, toLatitude);
	}

	public static double getDistanceMeter(double fromLongitude, double fromLatitude, double toLongitude,
		double toLatitude) {
		return getDistance(fromLongitude, fromLatitude, toLongitude, toLatitude) * 1000.0;
	}

	public static double kmToMeter(double distance) {
		return distance * 1000.0;
	}

	public static double meterToKm(double distance) {
		return distance / 1000.0;
	}

	/*
	 * 1. 기준점 from을 중심으로 하는 원 또는 사각형이 있다고 가정한다.
	 * 2. 그 사각형의 가장 좌측 X 좌표와 가장 우측 X 좌표를 구한다. 다시 말해, 현재 주어진 거리에서 최소 값과 최대 값을 도출한다.
	 * 3. from을 기준으로 좌우를 구하는 것이기 때문에 범위(meter)를 절반으로 나눈 뒤 공식을 통해 경도를 추측한다.
	 * 4. 공식은 getDistance에서 사용된 공식에서 위도를 0으로 했을 때, longCalParam을 구하는데 사용된 공식을 거꾸로 활용하여 도출한다.
	 * 5. 오차가 존재할 수 있으므로 10%의 오차 범위를 도입한다.
	 * */
	public static MapRangeInfo getLocationPoint(double fromLongitude, double fromLatitude, int meter) {
		double[] longValues = getLongitudeValues(fromLongitude, meter);
		double[] latValues = getLatitudeValues(fromLatitude, meter);
		return MapRangeInfo.builder()
			.fromLongitude(fromLongitude)
			.fromLatitude(fromLatitude)
			.minLongitude(longValues[0])
			.maxLongitude(longValues[1])
			.minLatitude(latValues[0])
			.maxLatitude(latValues[1])
			.build();
	}

	public static double[] getLongitudeValues(double fromLongitude, double meter) {
		double distanceValue = meterToKm(meter / 2.0);

		int degree = (int)(distanceValue / LONG_DEGREE_CONST);
		distanceValue = distanceValue - (degree * LONG_DEGREE_CONST);

		int minute = (int)(distanceValue / LONG_MINUTE_CONST);
		distanceValue = distanceValue - (minute * LONG_MINUTE_CONST);

		double second = distanceValue / LONG_SECOND_CONST;

		Object[] longValues = generatedPointToValue(fromLongitude);
		int resultDegree = (int)longValues[0] + degree;
		int resultMinute = (int)longValues[1] + minute;
		double resultMinSecond = (double)longValues[2] - second;
		double resultMaxSecond = (double)longValues[2] + second;

		double minLongitude = generatedValueToPoint(resultDegree, resultMinute, resultMinSecond);
		double maxLongitude = generatedValueToPoint(resultDegree, resultMinute, resultMaxSecond);

		return new double[] {minLongitude, maxLongitude};
	}

	public static double[] getLatitudeValues(double fromLatitude, int meter) {
		double distanceValue = meterToKm(meter / 2.0);

		int degree = (int)(distanceValue / LAT_DEGREE_CONST);
		distanceValue = distanceValue - (degree * LAT_DEGREE_CONST);

		int minute = (int)(distanceValue / LAT_MINUTE_CONST);
		distanceValue = distanceValue - (minute * LAT_MINUTE_CONST);

		double second = distanceValue / LAT_SECOND_CONST;

		Object[] latValues = generatedPointToValue(fromLatitude);
		int resultDegree = (int)latValues[0] + degree;
		int resultMinute = (int)latValues[1] + minute;
		double resultMinSecond = (double)latValues[2] - second;
		double resultMaxSecond = (double)latValues[2] + second;

		double minLatitude = generatedValueToPoint(resultDegree, resultMinute, resultMinSecond);
		double maxLatitude = generatedValueToPoint(resultDegree, resultMinute, resultMaxSecond);

		return new double[] {minLatitude, maxLatitude};
	}

	private static double getDistance(double fromLongitude, double fromLatitude, double toLongitude,
		double toLatitude) {
		Double[] from = new Double[] {fromLongitude, fromLatitude};
		Double[] to = new Double[] {toLongitude, toLatitude};
		Double[] gap = getGeoCodeGap(from, to);

		//Longitude, 경도
		Object[] longValues = generatedPointToValue(gap[0]);
		int longDegree = (int)longValues[0];
		int longArcMinute = (int)longValues[1];
		double longArcSecond = (double)longValues[2];

		//Latitude, 위도
		Object[] latValues = generatedPointToValue(gap[1]);
		int latDegree = (int)latValues[0];
		int latArcMinute = (int)latValues[1];
		double latArcSecond = (double)latValues[2];

		double longCalParam = (longDegree * LONG_DEGREE_CONST) + (longArcMinute * LONG_MINUTE_CONST) + (longArcSecond
			* LONG_SECOND_CONST);
		double latCalParam =
			(latDegree * LAT_DEGREE_CONST) + (latArcMinute * LAT_MINUTE_CONST) + (latArcSecond * LAT_SECOND_CONST);

		//두 점 사이의 거리 구하는 공식 적용
		return Math.sqrt(Math.pow(latCalParam, 2) + Math.pow(longCalParam, 2));
	}

	private static Double[] getGeoCodeGap(Double[] fromPoint, Double[] toPoint) {
		double longitude = fromPoint[0] - toPoint[0];
		double latitude = fromPoint[1] - toPoint[1];
		return new Double[] {longitude, latitude};
	}

	//도분초 구하기 degree : 도, arcMinute : 분, arcSecond : 초
	private static Object[] generatedPointToValue(double point) {
		int degreeIntValue = (int)point;
		double degreeMinorValue = point - degreeIntValue;
		double tempArcMinuteValue = degreeMinorValue * 60;
		int arcMinuteIntValue = (int)tempArcMinuteValue;
		double arcSecondIntValue = generatedBelowPoint((tempArcMinuteValue - arcMinuteIntValue) * 60, 2);
		return new Object[] {degreeIntValue, arcMinuteIntValue, arcSecondIntValue};
	}

	private static double generatedValueToPoint(int degree, int minute, double second) {
		return degree + ((minute / 60.0) + (second / 3600.0));
	}

	private static double generatedBelowPoint(double value, int decimal) {
		int operand = (int)Math.pow(10, decimal);
		return (double)Math.round(value * operand) / operand;
	}
}
