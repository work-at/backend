package com.workat.api.map.dto;

import com.workat.api.user.dto.DepartmentTypeDto;
import com.workat.api.user.dto.DurationTypeDto;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerDto {

	private long id;

	private String imageUrl;

	private DepartmentTypeDto position;

	private DurationTypeDto workingYear;

	private WorkerDto(long id, String imageUrl, DepartmentType position, DurationType workingYear) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.position = DepartmentTypeDto.of(position.name(), position.getType());
		this.workingYear = DurationTypeDto.of(workingYear.name(), workingYear.getType(), workingYear.getDescription());
	}

	public static WorkerDto of(long id, String imageUrl, DepartmentType position, DurationType workingYear) {
		return new WorkerDto(id, imageUrl, position, workingYear);
	}

}
