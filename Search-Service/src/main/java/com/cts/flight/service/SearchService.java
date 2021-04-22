package com.cts.flight.service;

import java.time.LocalDate;
import java.util.List;

import com.cts.flight.entity.Flight;
import com.cts.flight.model.SearchQuery;

public interface SearchService {

	List<Flight> findFlights(String origin, String destination, LocalDate flightDate, int numberofPassengers);

	List<Flight> searchFlights(SearchQuery query);

	Flight findById(int id);

	Flight findByFlightNumberAndFlightDateAndOriginAndDestination(String flightNumber, LocalDate flightDate,
			String origin, String destination);

	void updateInventory(String flightNumber, LocalDate flightDate, String origin, String destination,
			int newInventory);

}