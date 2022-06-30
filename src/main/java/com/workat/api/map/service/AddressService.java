package com.workat.api.map.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.workat.common.exception.base.BusinessException;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.http.LocationHttpReceiver;
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

	public WorkerLocation saveAddress(String userId, String longitude, String latitude) {
		KakaoAddressResponse response = locationHttpReceiver.getAddress(longitude, latitude);

		if (response.getMeta().getTotalCount() == 0) {
			throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, "위도 경도를 주소로 바꿀 수 없습니다.");
		}
		String address = response.getDocuments().get(0).getAddress().getEntireAddress();

		WorkerLocation workerLocation = WorkerLocation.of(userId, longitude, latitude, address);
		return workerLocationRedisRepository.save(workerLocation);
	}
}
