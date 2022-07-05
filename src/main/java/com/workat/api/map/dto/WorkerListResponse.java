package com.workat.api.map.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerListResponse {

	private List<WorkerResponse> response;

	private WorkerListResponse(List<WorkerResponse> response) {
		this.response = response;
	}

	public static WorkerListResponse of(List<WorkerResponse> response) {
		return new WorkerListResponse(response);
	}
}
