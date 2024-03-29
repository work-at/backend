package com.workat.api.map.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workat.api.user.dto.ActivityTypeDto;
import com.workat.api.user.dto.DepartmentTypeDto;
import com.workat.api.user.dto.DurationTypeDto;
import com.workat.domain.user.entity.UserProfile;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkerDto {

	@ApiModelProperty(name = "id", notes = "워케이셔너 id", example = "1")
	private long id;

	@ApiModelProperty(name = "nickname", notes = "워케이셔너 닉네임", example = "이름여덟글자제한")
	private String nickname;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty(name = "imageUrl", notes = "워케이셔너 사진 이미지 주소", example = "https://avatars.githubusercontent.com/u/46469385?v=4")
	private String imageUrl;

	@ApiModelProperty(name = "position", notes = "워케이셔너 직무", example = "{\"name\":\"ENGINEER\",\"content\":\"IT 엔지니어 및 보안\"}")
	private DepartmentTypeDto position;

	@ApiModelProperty(name = "workingYear", notes = "워케이셔너 연차", example = "{\"name\":\"JUNIOR\",\"content\":\"주니어(1~4년)\"}")
	private DurationTypeDto workingYear;

	@ApiModelProperty(name = "company", notes = "유저 회사, 미인증시 null", example = "emarteveryday")
	private String company;

	@ApiModelProperty(name = "story", notes = "유저 자기소개", example = "안녕하세요 이번에 워케이션에 놀러왔어요")
	private String story;

	@ApiModelProperty(name = "workchats", notes = "워크챗 수", example = "5")
	private int workchats;

	@ApiModelProperty(name = "activities", notes = "희망 활동")
	private List<ActivityTypeDto> activities;

	private WorkerDto(long id, String nickname, String imageUrl, DepartmentType position, DurationType workingYear,
		String company, String story, int workchats, List<ActivityTypeDto> activities, String baseUrl) {
		this.id = id;
		this.nickname = nickname;
		this.imageUrl = imageUrl == null || imageUrl.isBlank() ? null : baseUrl + imageUrl;
		this.position = DepartmentTypeDto.of(position.name(), position.getType());
		this.workingYear = DurationTypeDto.of(workingYear.name(), workingYear.getType());
		this.company = company;
		this.story = story;
		this.workchats = workchats;
		this.activities = activities;
	}

	public static WorkerDto of(UserProfile userProfile, int workchats, List<ActivityTypeDto> activities, String baseUrl) {
		return new WorkerDto(userProfile.getId(), userProfile.getNickname(), userProfile.getImageUrl(),
			userProfile.getPosition(), userProfile.getWorkingYear(), userProfile.getCompany(), userProfile.getStory(),
			workchats, activities, baseUrl);
	}

}
