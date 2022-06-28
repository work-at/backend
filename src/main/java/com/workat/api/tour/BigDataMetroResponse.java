package com.workat.api.tour;

import static lombok.AccessLevel.*;

import com.workat.api.tour.model.BigDataResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataMetroResponse {

	private BigDataResponse response;

	private BigDataMetroResponse(BigDataResponse response) {
		this.response = response;
	}

	public static BigDataMetroResponse of(BigDataResponse response) {
		return new BigDataMetroResponse(response);
	}
}
