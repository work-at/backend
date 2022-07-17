package com.workat.api.map.dto.response;

import java.util.List;

import com.workat.api.map.dto.WorkerDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerListResponse {

	@ApiModelProperty(name = "response", notes = "주위 유저 목록", example = "[{\"id\":3,\"imageUrl\":null,\"position\":{\"name\":\"IT_ENGINEER\",\"content\":\"IT 엔지니어 및 보안\"},\"workingYear\":{\"name\":\"JUNIOR\",\"content\":\"주니어(1~4년)\"}}]")
	private List<WorkerDto> response;

	private WorkerListResponse(List<WorkerDto> response) {
		this.response = response;
	}

	public static WorkerListResponse of(List<WorkerDto> response) {
		return new WorkerListResponse(response);
	}

}
