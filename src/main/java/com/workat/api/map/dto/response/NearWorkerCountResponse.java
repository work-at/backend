package com.workat.api.map.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NearWorkerCountResponse {

	@ApiModelProperty(name = "address", notes = "유저 현재 주소", example = "경기 안성시 죽산면 죽산리")
	private String address;

	@ApiModelProperty(name = "count", notes = "내 주위 워케이셔너 수", example = "7")
	private int count;

	private NearWorkerCountResponse(String address, int count) {
		this.address = address;
		this.count = count;
	}

	public static NearWorkerCountResponse of(String address, int count) {
		return new NearWorkerCountResponse(address, count);
	}
}
