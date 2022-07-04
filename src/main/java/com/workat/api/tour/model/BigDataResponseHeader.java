package com.workat.api.tour.model;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BigDataResponseHeader {

	private String resultCode;

	private String resultMsg;
}
