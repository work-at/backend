package com.workat.domain.tag.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TagCountDto {

	@ApiModelProperty(name = "review tag", example = "{ \"name\": \"VIEW\", \"content\": \"뷰가 좋아요\" }")
	private TagDto tag;

	@ApiModelProperty(name = "review count", example = "3")
	private long count;

	private TagCountDto(TagDto tag, long count) {
		this.tag = tag;
		this.count = count;
	}

	public static TagCountDto of(TagSummaryDto tag, long count) {
		return new TagCountDto(tag, count);
	}

	public static TagCountDto of(TagContentDto tag, long count) {
		return new TagCountDto(tag, count);
	}

	public static TagCountDto of(TagInfoDto tag, long count) {
		return new TagCountDto(tag, count);
	}
}
