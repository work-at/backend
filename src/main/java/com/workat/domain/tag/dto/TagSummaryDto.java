package com.workat.domain.tag.dto;

import com.workat.domain.tag.ReviewTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TagSummaryDto extends TagDto{

	@ApiModelProperty(name = "name", notes = "tag name", example = "VIEW")
	private String name;

	@ApiModelProperty(name = "content", notes = "tag summary (축약 필드)", example = "뷰가좋아요")
	private String content;

	private TagSummaryDto(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public static TagSummaryDto of(ReviewTag baseReviewData) {
		return new TagSummaryDto(baseReviewData.getName(), baseReviewData.getSummary());
	}

}
