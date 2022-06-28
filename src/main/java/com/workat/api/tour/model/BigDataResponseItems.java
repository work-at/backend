package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponseItems {

	private List<BigDataResponseItem> item;

	private BigDataResponseItems(List<BigDataResponseItem> item) {
		this.item = item;
	}

	public static BigDataResponseItems of(List<BigDataResponseItem> item) {
		return new BigDataResponseItems(item);
	}
}
