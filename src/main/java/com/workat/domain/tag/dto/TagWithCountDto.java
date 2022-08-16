package com.workat.domain.tag.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TagWithCountDto {

	@ApiModelProperty(name = "review tag", example = "{ \"name\": \"VIEW\", \"content\": \"뷰가 좋아요\" }")
	private TagDto tag;

	@ApiModelProperty(name = "review count", example = "3")
	private int count;

	private TagWithCountDto(TagDto tag, int count) {
		this.tag = tag;
		this.count = count;
	}

	public static TagWithCountDto of(TagDto tag, int count) {
		return new TagWithCountDto(tag, count);
	}
}
