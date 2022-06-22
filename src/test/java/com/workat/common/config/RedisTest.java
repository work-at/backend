package com.workat.common.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisTest {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void setRedisKeyValue() {
		ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();

		String key = "key_test";
		String value = "value_test";
		stringValueOperations.set(key, value);

		Assertions.assertEquals(value, stringValueOperations.get(key));
	}
}
