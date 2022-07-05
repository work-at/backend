package com.workat.api.tour;

import static lombok.AccessLevel.*;

import com.workat.api.tour.model.BigDataResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataLocalResponse {

	private BigDataResponse response;

	private BigDataLocalResponse(BigDataResponse response) {
		this.response = response;
	}

	public static BigDataLocalResponse of(BigDataResponse response) {
		return new BigDataLocalResponse(response);
	}
}
