package com.workat.api.user.dto.request;

import static lombok.AccessLevel.*;

import javax.validation.constraints.Pattern;

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

	@ApiModelProperty(name = "position", notes = "직무 타입", example = "\"IT_ENGINEER\"")
	private DepartmentType position;

	@ApiModelProperty(name = "workingYear", notes = "연차 타입", example = "\"JUNIOR\"")
	private DurationType workingYear;

	@ApiModelProperty(name = "story", notes = "유저 자기소개", example = "안녕하세요 이번에 워케이션에 놀러왔어요")
	private String story;

	//TODO: 희망 활동 추가

	private UserUpdateRequest(String nickname, DepartmentType position, DurationType workingYear, String story) {
		this.nickname = nickname;
		this.position = position;
		this.workingYear = workingYear;
		this.story = story;
	}
}
