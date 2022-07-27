package com.workat.api.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.map.dto.request.UserAddressRequest;
import com.workat.api.map.dto.response.UserAddressResponse;
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

	@ApiOperation(value = "save user address", notes = "유저 위도 경도 저장 및 주소 변환", authorizations = {@Authorization(value = "JWT 토큰")})
	@PostMapping("/api/v1/users/address")
	public ResponseEntity<UserAddressResponse> saveUserAddress(@UserValidation Users user, @RequestBody UserAddressRequest request) {
		UserAddressResponse response = addressService.saveUserAddress(user, request);
		return ResponseEntity.ok(response);
	}

}
