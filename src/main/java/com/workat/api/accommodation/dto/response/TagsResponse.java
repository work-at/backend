package com.workat.api.accommodation.dto.response;

import java.util.List;

import com.workat.domain.tag.dto.TagDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TagsResponse {

	@ApiModelProperty(name = "tags", example = "[ { \"name\": \"VIEW\", \"content\": \"뷰가 좋아요\" }, { \"name\": \"PARKING\", \"content\": \"주차하기 편해요~\" } ]")
	private List<TagDto> tags;

	private TagsResponse(List<TagDto> tags) {
		this.tags = tags;
	}

	public static TagsResponse of(List<TagDto> tags) {
		return new TagsResponse(tags);
	}
}
