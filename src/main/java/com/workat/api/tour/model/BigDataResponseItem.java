package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponseItem {

	private int baseYmd;
	private int dayOfWeekCode;
	private String dayOfWeekName;
	private int cityCode;
	private String cityName;
	private int touristCode;
	private String touristName;
	private double touristNum;

	@Builder
	public BigDataResponseItem(int baseYmd, int dayOfWeekCode, String dayOfWeekName, int cityCode, String cityName, int touristCode, String touristName, double touristNum) {
		this.baseYmd = baseYmd;
		this.dayOfWeekCode = dayOfWeekCode;
		this.dayOfWeekName = dayOfWeekName;
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.touristCode = touristCode;
		this.touristName = touristName;
		this.touristNum = touristNum;
	}
}
