package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponseBody {

	private BigDataResponseItems items;
	private int numOfRows;
	private int pageNo;
	private int totalCount;

	@Builder
	public BigDataResponseBody(BigDataResponseItems items, int numOfRows, int pageNo, int totalCount) {
		this.items = items;
		this.numOfRows = numOfRows;
		this.pageNo = pageNo;
		this.totalCount = totalCount;
	}
}
