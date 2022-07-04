package com.workat.api.map.dto;

import com.workat.api.user.dto.DepartmentTypeResponse;
import com.workat.api.user.dto.DurationTypeResponse;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerResponse {

	private String id;

	private String imageUrl;

	private DepartmentTypeResponse position;

	private DurationTypeResponse workingYear;

	private WorkerResponse(String id, String imageUrl, DepartmentType position, DurationType workingYear) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.position = DepartmentTypeResponse.of(position.name(), position.getType());
		this.workingYear = DurationTypeResponse.of(workingYear.name(), workingYear.getType(), workingYear.getDescription());
	}

	public static WorkerResponse of(String id, String imageUrl, DepartmentType position, DurationType workingYear) {
		return new WorkerResponse(id, imageUrl, position, workingYear);
	}

}
