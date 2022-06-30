package com.workat.domain.map.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoAddressDto {

	private String addressName;

	@JsonProperty("region_1depth_name")
	private String region1DepthName;

	@JsonProperty("region_2depth_name")
	private String region2DepthName;

	@JsonProperty("region_3depth_name")
	private String region3DepthName;

	private String mountainYn;

	private String mainAddressNo;

	private String subAddressNo;

	private String zipCode;

}
