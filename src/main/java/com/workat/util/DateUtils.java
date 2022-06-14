package com.workat.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	public static String convertDate(Long timeMills) {
		return new SimpleDateFormat("yyyyMMdd").format(timeMills);
	}

	public static String convertDate(LocalDate localDate) {
		return DateTimeFormatter.ofPattern("yyyyMMdd").format(localDate);
	}

	public static String convertDate(LocalDateTime localDateTime) {
		return DateTimeFormatter.ofPattern("yyyyMMdd").format(localDateTime);
	}

	public static String convertDateTime(Long timeMills) {
		return new SimpleDateFormat("yyyyMMddHHmm").format(timeMills);
	}

	public static String convertDateTime(LocalDateTime localDateTime) {
		return DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(localDateTime);
	}
}
