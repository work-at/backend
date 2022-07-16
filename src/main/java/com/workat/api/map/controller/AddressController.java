package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.request.NearWorkerCountRequest;
import com.workat.api.map.dto.response.NearWorkerCountResponse;
import com.workat.api.map.service.AddressService;
import com.workat.common.annotation.UserValidation;
import com.workat.domain.user.entity.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "User Address Api")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AddressController {

	private final AddressService addressService;

	@ApiOperation(value = "getAddressAndNearWorkerCount", notes = "맵 첫 진입 시 유저 주소 반환 및 주위 워케이셔너 수 반환", authorizations = {@Authorization(value = "JWT 토큰")})
	@PostMapping("/api/v1/map")
	public ResponseEntity<NearWorkerCountResponse> getAddressAndNearWorkerCount(@UserValidation Users user, @RequestBody NearWorkerCountRequest request) {
		NearWorkerCountResponse response = addressService.getAddressAndNearWorkerCount(user, request);
		return ResponseEntity.ok(response);
	}

}
