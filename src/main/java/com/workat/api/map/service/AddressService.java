package com.workat.api.map.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.workat.common.exception.base.BusinessException;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoAddressDocumentDto;
import com.workat.domain.map.http.dto.KakaoAddressDto;
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.repository.WorkerLocationRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressService {

	private final LocationHttpReceiver locationHttpReceiver;

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	public void saveAddress(Long userId, String longitude, String latitude) {
		KakaoAddressResponse response = locationHttpReceiver.getAddress(longitude, latitude);

		if (response.getMeta().getTotalCount() == 0) {
			throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, "위도 경도를 주소로 바꿀 수 없습니다.");
		}

		String address = Optional.of(response)
			.map(KakaoAddressResponse::getDocuments)
			.map(d -> d.get(0))
			.map(KakaoAddressDocumentDto::getAddress)
			.map(KakaoAddressDto::getEntireAddress).orElseThrow(() -> new BusinessException(HttpStatus.NO_CONTENT, "위도 경도를 주소로 바꿀 수 없습니다."));

		WorkerLocation workerLocation = WorkerLocation.of(userId, longitude, latitude, address);
		workerLocationRedisRepository.save(workerLocation);
	}
}
