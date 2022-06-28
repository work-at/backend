package com.workat.domain.map.http.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KakaoLocalResponse {

	private List<KakaoLocalDataDto> documents;

	private KakaoLocalMetaDto meta;
}
