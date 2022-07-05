package com.workat.api.map.dto.response;

import java.util.List;

import com.workat.api.map.dto.WorkerDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerListResponse {

	private List<WorkerDto> response;

	private WorkerListResponse(List<WorkerDto> response) {
		this.response = response;
	}

	public static WorkerListResponse of(List<WorkerDto> response) {
		return new WorkerListResponse(response);
	}
}
