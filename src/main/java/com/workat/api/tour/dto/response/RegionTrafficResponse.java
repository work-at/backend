package com.workat.api.tour.dto.response;

import com.workat.api.tour.dto.VisitorDegree;
import com.workat.domain.accommodation.RegionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegionTrafficResponse {

	@ApiModelProperty(name = "region", notes = "SEOUL")
	private RegionType region;

	@ApiModelProperty(name = "type", notes = "POPULAR")
	private String type;

	@ApiModelProperty(name = "message", notes = "완전핫함")
	private String message;

	private RegionTrafficResponse(RegionType region, String type, String message) {
		this.region = region;
		this.type = type;
		this.message = message;
	}

	public static RegionTrafficResponse of(RegionType region, VisitorDegree degree) {
		return new RegionTrafficResponse(region, degree.name(), degree.getMessage());
	}
}
