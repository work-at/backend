package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponseItems {

	private List<BigDataResponseItem> item;

	@Builder
	public BigDataResponseItems(List<BigDataResponseItem> item) {
		this.item = item;
	}
}
