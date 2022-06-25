package com.workat.domain.map.http.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
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

}
