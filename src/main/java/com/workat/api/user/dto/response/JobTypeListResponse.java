package com.workat.api.user.dto.response;

import java.util.List;

import com.workat.api.user.dto.JobTypeDto;

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
