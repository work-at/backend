package com.workat.api.tour;

import static lombok.AccessLevel.*;

import com.workat.api.tour.model.BigDataResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataMetroResponse {

	private BigDataResponse response;

	@Builder
	public BigDataMetroResponse(BigDataResponse response) {
		this.response = response;
	}
}
