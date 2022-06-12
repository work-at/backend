package com.workat.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "sample")
@RestController
public class SampleController {

	@GetMapping(value = "/api/sample", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity sample(@RequestParam String param) {
		return ResponseEntity.ok(param);
	}
}
