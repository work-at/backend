package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.workat.common.annotation.IsValidListSize;
import com.workat.domain.user.job.DepartmentType;
import com.workat.domain.user.job.DurationType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserUpdateRequest {

	@Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|]{2,8}$", message = "닉네임은 2~8글자, 한글/영어/숫자만 가능합니다")
	@ApiModelProperty(name = "nickname", notes = "유저 닉네임", example = "\"나는스벅라떼좋아\"")
	private String nickname;

	@NotNull(message = "직무는 필수 값입니다.")
	@ApiModelProperty(name = "position", notes = "직무 타입")
	private DepartmentType position;

	@NotNull(message = "연차는 필수 값입니다.")
	@ApiModelProperty(name = "workingYear", notes = "연차 타입", example = "\"JUNIOR\"")
	private DurationType workingYear;

	@ApiModelProperty(name = "story", notes = "유저 자기소개", example = "안녕하세요 이번에 워케이션에 놀러왔어요")
	private String story;

	@ApiModelProperty(name = "activities", notes = "유저 활동 타입")
	@IsValidListSize(max = 3, message = "1개 이상 3개 이하의 리스트가 인풋으로 들어와야합니다")
	private List<String> activities;

	private UserUpdateRequest(String nickname, DepartmentType position, DurationType workingYear, String story,
		List<String> activities) {
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
		this.story = story;
		this.activities = activities;
	}
}
