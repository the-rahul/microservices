package com.rk.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class TestService {

	@Value("${name}")
	private String personName;
	
	@Bean
	public void f1() {
		System.out.println(">>>>>>>>> Name: " + personName);
	}
	
}
