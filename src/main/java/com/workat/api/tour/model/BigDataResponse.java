package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponse {

	private BigDataResponseHeader header;
	private BigDataResponseBody body;

	private BigDataResponse(BigDataResponseHeader header, BigDataResponseBody body) {
		this.header = header;
		this.body = body;
	}

	public static BigDataResponse of(BigDataResponseHeader header, BigDataResponseBody body) {
		return new BigDataResponse(header, body);
	}
}
