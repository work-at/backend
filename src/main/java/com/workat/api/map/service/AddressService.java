package com.workat.api.map.service;

import static org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.*;

import java.util.Optional;

import org.springframework.data.geo.Distance;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.workat.api.map.dto.request.NearWorkerCountRequest;
import com.workat.api.map.dto.response.NearWorkerCountResponse;
import com.workat.common.exception.NotFoundException;
import com.workat.common.exception.base.BusinessException;
import com.workat.domain.map.entity.WorkerLocation;
import com.workat.domain.map.http.LocationHttpReceiver;
import com.workat.domain.map.http.dto.KakaoAddressDocumentDto;
import com.workat.domain.map.http.dto.KakaoAddressDto;
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

	public NearWorkerCountResponse getAddressAndNearWorkerCount(Users user, NearWorkerCountRequest request) {
		getAddressAndSave(user.getId(), request.getLongitude(), request.getLatitude());

		WorkerLocation worker = workerLocationRedisRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("user not found"));
		int count = workerLocationRedisRepository.findAllByLocationNear(worker.getLocation(), new Distance(request.getKilometer(), KILOMETERS)).size();
		count = Math.max(count - 1, 0);

		return NearWorkerCountResponse.of(worker.getAddress(), count);
	}

	private void getAddressAndSave(Long userId, String longitude, String latitude) {
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
