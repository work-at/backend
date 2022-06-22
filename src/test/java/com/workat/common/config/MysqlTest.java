package com.workat.common.config;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MysqlTest {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/work_at?useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "password";

	@Test
	void testMysqlConnection() {
		Assertions.assertDoesNotThrow(this::connect);
	}

	private void connect() throws Exception {
		Class.forName(DRIVER);

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
			System.out.println(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
