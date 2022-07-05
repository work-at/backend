package com.workat.api.map.dto.response;

import com.workat.api.user.dto.DepartmentTypeDto;
import com.workat.api.user.dto.DurationTypeDto;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerDetailResponse {

	private Long id;

	private String imageUrl;

	private DepartmentTypeDto position;

	private DurationTypeDto workingYear;

	private String story;

	private WorkerDetailResponse(Long id, String imageUrl, DepartmentType position, DurationType workingYear, String story) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.position = DepartmentTypeDto.of(position.name(), position.getType());
		this.workingYear = DurationTypeDto.of(workingYear.name(), workingYear.getType(), workingYear.getDescription());
		this.story = story;
	}

	public static WorkerDetailResponse of(Long id, String imageUrl, DepartmentType position, DurationType workingYear, String story) {
		return new WorkerDetailResponse(id, imageUrl, position, workingYear, story);
	}

}
