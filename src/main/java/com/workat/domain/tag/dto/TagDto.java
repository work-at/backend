package com.workat.domain.tag.dto;

import com.workat.domain.tag.AccommodationReviewTag;
import com.workat.domain.tag.BaseTag;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TagDto {

	@ApiModelProperty(name = "name", notes = "tag name", example = "VIEW")
	private String name;

	@ApiModelProperty(name = "content", notes = "tag content", example = "뷰가 좋아요")
	private String content;

	private TagDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static TagDto of(BaseTag baseReviewData) {
		return new TagDto(baseReviewData.getName(), baseReviewData.getContent());
	}

	public static TagDto summaryOf(AccommodationReviewTag accommodationReviewTag) {
		return new TagDto(accommodationReviewTag.getName(), accommodationReviewTag.getSummary());
	}
}
