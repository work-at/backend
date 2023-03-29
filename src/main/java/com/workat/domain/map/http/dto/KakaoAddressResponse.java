package com.workat.domain.map.http.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoAddressResponse {

	private KakaoAddressMetaDto meta;

	private List<KakaoAddressDocumentDto> documents;

	public String convertToAddress() {
		return Optional.of(this)
			.map(KakaoAddressResponse::getDocuments)
			.map(d -> d.get(0))
			.map(KakaoAddressDocumentDto::getAddress)
			.map(KakaoAddressDto::getEntireAddress)
			.orElseThrow(() -> new BusinessException(HttpStatus.NO_CONTENT, "위도 경도를 주소로 바꿀 수 없습니다."));
	}
}
