package com.workat.api.map.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NearWorkerCountRequest {

	private String longitude;

	private String latitude;

	private double kilometer;

	private NearWorkerCountRequest(String longitude, String latitude, double kilometer) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.kilometer = kilometer;
	}

	public static NearWorkerCountRequest of(String longitude, String latitude, double kilometer) {
		return new NearWorkerCountRequest(longitude, latitude, kilometer);
	}

}
