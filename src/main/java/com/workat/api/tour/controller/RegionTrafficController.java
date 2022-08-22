package com.workat.api.tour.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workat.api.tour.dto.response.RegionTrafficResponse;
import com.workat.api.tour.service.TourApiService;
import com.workat.domain.accommodation.RegionType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Region Traffic Api")
@RequiredArgsConstructor
@RestController
public class RegionTrafficController {

	private final TourApiService tourApiService;

	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "관광 api 연동 지역 혼잡도 조회", notes = "지역 혼잡도를 랜덤으로 조회합니다")
	@GetMapping("/api/v1/region-traffic")
	public ResponseEntity<RegionTrafficResponse> getRandomRegionTraffic() {
		RegionType random = RegionType.getRandom();
		RegionTrafficResponse response = tourApiService.calculateWorkatDegree(random);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
