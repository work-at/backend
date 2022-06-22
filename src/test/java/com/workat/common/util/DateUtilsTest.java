package com.workat.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

	private static String TODAY_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

	private static String TODAY_DATE_TIME = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

	@DisplayName("long 타입을 yyyyMMdd 형식 스트링으로 변환 테스트")
	@Test
	void convertTest1() {
		//given

		//when

		//then
		assertEquals(TODAY_DATE, DateUtils.convertDate(System.currentTimeMillis()));
	}

	@DisplayName("localDate 타입을 yyyyMMdd 형식 스트링으로 변환 테스트")
	@Test
	void convertTest2() {
		//given

		//when

		//then
		assertEquals(TODAY_DATE, DateUtils.convertDate(LocalDate.now()));
	}

	@DisplayName("long 타입을 yyyyMMddHHmm 형식 스트링으로 변환 테스트")
	@Test
	void convertTest4() {
		//given

		//when

		//then
		assertEquals(TODAY_DATE_TIME, DateUtils.convertDateTime(System.currentTimeMillis()));
	}

	@DisplayName("localDateTime 타입을 yyyyMMddHHmm 형식 스트링으로 변환 테스트")
	@Test
	void convertTest5() {
		//given

		//when

		//then
		assertEquals(TODAY_DATE_TIME, DateUtils.convertDateTime(LocalDateTime.now()));
	}
}
