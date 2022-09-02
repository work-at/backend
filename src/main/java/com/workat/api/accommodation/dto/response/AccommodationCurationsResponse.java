package com.workat.api.accommodation.dto.response;

import java.util.List;

import com.workat.api.accommodation.dto.AccommodationCuration;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccommodationCurationsResponse {

	@ApiModelProperty(name = "accommodations")
	private List<AccommodationCuration> accommodations;

	private AccommodationCurationsResponse(List<AccommodationCuration> accommodations) {
		this.accommodations = accommodations;
	}

	public static AccommodationCurationsResponse of(List<AccommodationCuration> accommodations) {
		return new AccommodationCurationsResponse(accommodations);
	}
}
