package com.workat.api.map.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAddressResponse {

	@ApiModelProperty(name = "address", notes = "유저 현재 주소", example = "경기 안성시 죽산면 죽산리")
	private String address;

	private UserAddressResponse(String address) {
		this.address = address;
	}

	public static UserAddressResponse of(String address) {
		return new UserAddressResponse(address);
	}
}
