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
public class WorkerDto {

	private Long id;

	private String imageUrl;

	private DepartmentTypeResponse position;

	private DurationTypeResponse workingYear;

	private WorkerDto(Long id, String imageUrl, DepartmentType position, DurationType workingYear) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.position = DepartmentTypeResponse.of(position.name(), position.getType());
		this.workingYear = DurationTypeResponse.of(workingYear.name(), workingYear.getType(), workingYear.getDescription());
	}

	public static WorkerDto of(Long id, String imageUrl, DepartmentType position, DurationType workingYear) {
		return new WorkerDto(id, imageUrl, position, workingYear);
	}

}
