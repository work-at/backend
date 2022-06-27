package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponse {

	private BigDataResponseHeader header;
	private BigDataResponseBody body;

	@Builder
	public BigDataResponse(BigDataResponseHeader header, BigDataResponseBody body) {
		this.header = header;
		this.body = body;
	}
}
