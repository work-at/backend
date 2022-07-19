package com.workat.api.map.dto.response;

import com.workat.api.user.dto.DepartmentTypeDto;
import com.workat.api.user.dto.DurationTypeDto;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerDetailResponse {

	private long id;

	private String imageUrl;

	private DepartmentTypeDto position;

	private DurationTypeDto workingYear;

	private String story;

	private WorkerDetailResponse(long id, String imageUrl, DepartmentType position, DurationType workingYear,
		String story) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.position = DepartmentTypeDto.of(position.name(), position.getType());
		this.workingYear = DurationTypeDto.of(workingYear.name(), workingYear.getType());
		this.story = story;
	}

	public static WorkerDetailResponse of(UserProfile userProfile) {
		return new WorkerDetailResponse(userProfile.getId(), userProfile.getImageUrl(), userProfile.getPosition(), userProfile.getWorkingYear(), userProfile.getStory());
	}

}
