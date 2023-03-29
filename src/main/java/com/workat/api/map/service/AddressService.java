package com.workat.api.map.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.workat.api.map.dto.request.UserAddressRequest;
import com.workat.api.map.dto.response.UserAddressResponse;
import com.workat.common.exception.NotFoundException;
import com.workat.common.exception.base.BusinessException;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoAddressResponse;
import com.workat.domain.map.repository.worker.WorkerLocationRedisRepository;
import com.workat.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressService {

	private final LocationHttpReceiver locationHttpReceiver;

	private final WorkerLocationRedisRepository workerLocationRedisRepository;

	public UserAddressResponse saveUserAddress(Users user, UserAddressRequest request) {
		KakaoAddressResponse kakaoAddress = fetchKakaoAddress(request.getLongitude(), request.getLatitude());

		String address = kakaoAddress.convertToAddress();

		saveLocation(user.getId(), request.getLongitude(), request.getLatitude(), address);

		WorkerLocation worker = workerLocationRedisRepository.findById(user.getId())
			.orElseThrow(() -> new NotFoundException("user not found"));

		return UserAddressResponse.of(worker.getAddress());
	}

	private KakaoAddressResponse fetchKakaoAddress(String longitude, String latitude) {
		KakaoAddressResponse response = locationHttpReceiver.getAddress(longitude, latitude);

		if (response.getMeta().getTotalCount() == 0) {
			throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, "위도 경도를 주소로 바꿀 수 없습니다.");
		}

		return response;
	}

	private void saveLocation(Long userId, String longitude, String latitude, String address) {
		WorkerLocation workerLocation = WorkerLocation.of(userId, longitude, latitude, address);
		
		workerLocationRedisRepository.save(workerLocation);
	}
}
