package com.workat.api.user.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JobTypeListResponse {

	List<JobTypeDto> response;

	private JobTypeListResponse(List<JobTypeDto> response) {
		this.response = response;
	}

	public static JobTypeListResponse of(List<JobTypeDto> response) {
		return new JobTypeListResponse(response);
	}
}
