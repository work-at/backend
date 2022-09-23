package com.workat.domain.tag.dto;

import com.workat.api.review.dto.ReviewDto;
import com.workat.api.review.dto.ReviewTypeDto;
import com.workat.domain.tag.ReviewTag;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TagContentDto extends TagDto {

	@ApiModelProperty(name = "name", notes = "tag name", example = "VIEW")
	private String name;

	@ApiModelProperty(name = "content", notes = "tag content (줄바꿈 있는 필드)", example = "뷰가\n좋아요")
	private String content;

	@ApiModelProperty(name = "content2", notes = "tag content2 (줄바꿈 없는 필드)", example = "뷰가 좋아요")
	private String content2;

	@ApiModelProperty(name = "content3", notes = "tag content3 (축약된 필드)", example = "뷰가좋아요")
	private String content3;

	private TagContentDto(String name, String content, String content2, String content3) {
		this.name = name;
		this.content = content;
		this.content2 = content2;
		this.content3 = content3;
	}

	public static TagContentDto of(ReviewTag baseReviewData) {
		return new TagContentDto(baseReviewData.getName(), baseReviewData.getContent(), baseReviewData.getContent2(), baseReviewData.getSummary());
	}

}
