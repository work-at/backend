package com.workat.api.map.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAddressRequest {

	private String longitude;

	private String latitude;

	private UserAddressRequest(String longitude, String latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public static UserAddressRequest of(String longitude, String latitude) {
		return new UserAddressRequest(longitude, latitude);
	}

}
