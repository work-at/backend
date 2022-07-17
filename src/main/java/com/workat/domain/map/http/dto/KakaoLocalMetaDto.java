package com.workat.domain.map.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class KakaoLocalMetaDto {

	@JsonProperty("is_end")
	private boolean isEnd;

	@JsonProperty("pageable_count")
	private int pageableCount;

	@JsonProperty("total_count")
	private int totalCount;

	public static KakaoLocalMetaDto of(boolean isEnd, int pageableCount, int totalCount) {
		KakaoLocalMetaDto dto = new KakaoLocalMetaDto();
		dto.isEnd = isEnd;
		dto.pageableCount = pageableCount;
		dto.totalCount = totalCount;
		return dto;
	}
}
