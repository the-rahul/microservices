package com.cts.flight.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.flight.entity.Flight;
import com.cts.flight.model.SearchQuery;
import com.cts.flight.service.SearchService;

@RestController
@CrossOrigin
@RequestMapping("/api/search")
public class SearchRestController {
	
	@Autowired
	private SearchService searchService;
	
	@PostMapping
	public List<Flight> searchFlights(@RequestBody SearchQuery query){
		return searchService.searchFlights(query);
	}
	
	@GetMapping("/{origin}/{destination}/{flightDate}/{numberofPassengers}")
	public List<Flight> findFlights(@PathVariable String origin,
			@PathVariable String destination,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate,
			@PathVariable int numberofPassengers){
	
		return searchService.findFlights(origin, destination, flightDate,numberofPassengers);
	}
	
	@GetMapping("/{id}")
	public Flight findFlight(@PathVariable int id) {
		return searchService.findById(id);
	}
	
	@GetMapping("findFlight/{flightNumber}/{flightDate}/{origin}/{destination}")
	public Flight findFlight(@PathVariable String flightNumber,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate flightDate,
			@PathVariable String origin,
			@PathVariable String destination){
		return searchService.findByFlightNumberAndFlightDateAndOriginAndDestination(flightNumber, flightDate, origin, destination);
	}
	
	
	
	
	

}
