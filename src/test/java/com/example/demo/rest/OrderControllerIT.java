package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {

    @Autowired
	private TestRestTemplate template;

    @Test
	public void getHello() throws Exception {
		ResponseEntity<String> response = template.getForEntity("/orders", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
