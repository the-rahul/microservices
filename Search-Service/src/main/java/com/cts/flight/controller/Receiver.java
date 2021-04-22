package com.cts.flight.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import com.cts.flight.service.SearchService;

@Controller
public class Receiver {

	@Autowired
	private SearchService searchService;

	@Bean
	public Queue q1() {
		return new Queue("inventoryQ", false);
	}

	
	@RabbitListener(queues = "inventoryQ")
	public void processMessage(Map<String, Object> data) {
		
		System.out.println(">>>>>>>>>>>>>>>>>> UPDATING INVENTORY IN SEARCH SERVICE <<<<<<<<<<<<<<<<<");

		searchService.updateInventory((String) data.get("FLIGHT_NUMBER"), (LocalDate) data.get("FLIGHT_DATE"),
				(String) data.get("ORIGIN"), (String) data.get("DESTINATION"), (int) data.get("NEW_INVENTORY"));

		System.out.println(">>>>>>>>>>>>>>>>>> INVENTORY IS UPDATED <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}

}
