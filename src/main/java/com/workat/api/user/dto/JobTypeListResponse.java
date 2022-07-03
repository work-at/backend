package com.workat.api.user.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JobTypeListResponse {

	List<JobTypeResponse> response;

	private JobTypeListResponse(List<JobTypeResponse> response) {
		this.response = response;
	}

	public static JobTypeListResponse of(List<JobTypeResponse> response) {
		return new JobTypeListResponse(response);
	}
}
