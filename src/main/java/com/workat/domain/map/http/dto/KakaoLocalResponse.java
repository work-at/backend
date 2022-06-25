package com.workat.domain.map.http.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoLocalResponse {

	private List<KakaoLocalDataDto> documents;

	private KakaoLocalMetaDto meta;
}
