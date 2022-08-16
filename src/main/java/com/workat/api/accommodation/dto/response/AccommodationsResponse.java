package com.workat.api.accommodation.dto.response;

import java.util.List;

import com.workat.api.accommodation.dto.AccommodationDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccommodationsResponse {

	@ApiModelProperty(name = "accommodations")
	private List<AccommodationDto> accommodations;

	@ApiModelProperty(name = "pageNumber", example = "0")
	private int pageNumber;

	@ApiModelProperty(name = "pageSize", example = "10")
	private int pageSize;

	@ApiModelProperty(name = "totalCount", example = "150")
	private int totalCount;

	private AccommodationsResponse(List<AccommodationDto> accommodations, int pageNumber, int pageSize,
		int totalCount) {
		this.accommodations = accommodations;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
	}

	public static AccommodationsResponse of(List<AccommodationDto> accommodations, int pageNumber, int pageSize,
		int totalCount) {
		return new AccommodationsResponse(accommodations, pageNumber, pageSize, totalCount);
	}
}
