package com.workat.domain.map.http.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class KakaoLocalMetaDto {

	private boolean isEnd;

	private int pageableCount;

	private int totalCount;

	public static KakaoLocalMetaDto of(boolean isEnd, int pageableCount, int totalCount) {
		KakaoLocalMetaDto dto = new KakaoLocalMetaDto();
		dto.isEnd = isEnd;
		dto.pageableCount = pageableCount;
		dto.totalCount = totalCount;
		return dto;
	}
}
