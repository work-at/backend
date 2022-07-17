package com.workat.api.map.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerSizeResponse {

	@ApiModelProperty(name = "count", notes = "주위 유저 수", example = "5")
	private int count;

	private WorkerSizeResponse(int count) {
		this.count = count;
	}

	public static WorkerSizeResponse of(int count) {
		return new WorkerSizeResponse(count);
	}
}
