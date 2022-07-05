package com.workat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.workat.domain.config.MultipleDatasourceBaseTest;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class WorkatApplicationTests extends MultipleDatasourceBaseTest{

	@Test
	void contextLoads() {
	}
}
