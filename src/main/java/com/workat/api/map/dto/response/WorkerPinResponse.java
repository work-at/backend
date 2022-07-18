package com.workat.api.map.dto.response;

import java.util.List;

import com.workat.api.map.dto.WorkerPinDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerPinResponse {

	@ApiModelProperty(name = "response", notes = "주위 유저 핀 목록", example = "")
	private List<WorkerPinDto> response;

	private WorkerPinResponse(List<WorkerPinDto> response) {
		this.response = response;
	}

	public static WorkerPinResponse of(List<WorkerPinDto> response) {
		return new WorkerPinResponse(response);
	}
}
