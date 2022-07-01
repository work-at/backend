package com.workat.domain.map.http.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoAddressResponse {

	private KakaoAddressMetaDto meta;

	private List<KakaoAddressDocumentDto> documents;

}
