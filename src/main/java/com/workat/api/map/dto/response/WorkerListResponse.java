package com.workat.api.map.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workat.api.map.dto.WorkerDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerListResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<WorkerDto> response;

	private int count;

	private WorkerListResponse(List<WorkerDto> response, int count) {
		this.response = response;
		this.count = count;
	}

	public static WorkerListResponse of(List<WorkerDto> response) {
		return new WorkerListResponse(response, response.size());
	}

	public static WorkerListResponse metaDataOf(int count) {
		return new WorkerListResponse(null, count);
	}
}
