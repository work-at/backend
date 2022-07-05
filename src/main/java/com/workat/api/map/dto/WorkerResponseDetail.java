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
public class WorkerResponseDetail {

	private String id;

	private String imageUrl;

	private DepartmentTypeDto position;

	private DurationTypeDto workingYear;

	private String story;

	private WorkerResponseDetail(String id, String imageUrl, DepartmentType position, DurationType workingYear, String story) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.position = DepartmentTypeDto.of(position.name(), position.getType());
		this.workingYear = DurationTypeDto.of(workingYear.name(), workingYear.getType(), workingYear.getDescription());
		this.story = story;
	}

	public static WorkerResponseDetail of(String id, String imageUrl, DepartmentType position, DurationType workingYear, String story) {
		return new WorkerResponseDetail(id, imageUrl, position, workingYear, story);
	}

}
