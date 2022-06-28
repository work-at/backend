package com.workat.domain.map.http.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoLocalDataDto {

	private String id;

	private String phone;

	private String placeName;

	private String placeUrl;

	private String x;

	private String y;

	private String addressName;

	private String roadAddressName;

	@Builder
	public KakaoLocalDataDto(String id, String phone, String placeName, String placeUrl, String x, String y,
		String addressName, String roadAddressName) {
		this.id = id;
		this.phone = phone;
		this.placeName = placeName;
		this.placeUrl = placeUrl;
		this.x = x;
		this.y = y;
		this.addressName = addressName;
		this.roadAddressName = roadAddressName;

	}

}
