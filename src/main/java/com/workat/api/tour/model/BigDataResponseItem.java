package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponseItem {

	@JsonProperty("baseYmd")
	private int baseYmd;

	@JsonProperty("daywkDivCd")
	private int dayOfWeekCode;

	@JsonProperty("daywkDivNm")
	private String dayOfWeekName;

	@JsonProperty("signguCode")
	@JsonAlias("areaCode")
	private int cityCode;

	@JsonProperty("signguNm")
	@JsonAlias("areaNm")
	private String cityName;

	@JsonProperty("touDivCd")
	private int touristCode;

	@JsonProperty("touDivNm")
	private String touristName;

	@JsonProperty("touNum")
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
