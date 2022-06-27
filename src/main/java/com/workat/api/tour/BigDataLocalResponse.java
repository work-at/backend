package com.workat.api.tour;

import static lombok.AccessLevel.*;

import com.workat.api.tour.model.BigDataResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataLocalResponse {

	private BigDataResponse response;

	@Builder
	public BigDataLocalResponse(BigDataResponse response) {
		this.response = response;
	}
}
