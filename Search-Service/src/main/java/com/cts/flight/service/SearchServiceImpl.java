package com.cts.flight.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.flight.dao.FlightDao;
import com.cts.flight.entity.Flight;
import com.cts.flight.entity.Inventory;
import com.cts.flight.model.SearchQuery;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private FlightDao flightDao;

	@Override
	public List<Flight> findFlights(String origin, String destination, LocalDate flightDate, int numberofPassengers) {
		List<Flight> flights = flightDao.findFlightByOriginAndDestinationAndFlightDate(origin, destination, flightDate);

		return flights.stream().filter(flight -> flight.getInventory().getCount() >= numberofPassengers)
				.collect(Collectors.toList());
	}

	@Override
	public List<Flight> searchFlights(SearchQuery query) {
		List<Flight> flights = flightDao.findFlightByOriginAndDestinationAndFlightDate(query.getOrigin(),
				query.getDestination(), query.getFlightDate());

		return flights.stream().filter(flight -> flight.getInventory().getCount() >= query.getNumberofPassengers())
				.collect(Collectors.toList());
	}

	@Override
	public Flight findById(int id) {
		return flightDao.findById(id).orElse(null);
	}

	@Override
	public Flight findByFlightNumberAndFlightDateAndOriginAndDestination(String flightNumber, LocalDate flightDate,
			String origin, String destination) {
		return flightDao.findByFlightNumberAndFlightDateAndOriginAndDestination(flightNumber, flightDate, origin,
				destination);
	}

	@Override
	public void updateInventory(String flightNumber, LocalDate flightDate, String origin, String destination,
			int newInventory) {

		Flight flight = findByFlightNumberAndFlightDateAndOriginAndDestination(flightNumber, flightDate, origin,
				destination);
		if (flight != null) {
			Inventory inv = flight.getInventory();
			inv.setCount(inv.getCount() - newInventory);
			flight.setInventory(inv);
			flightDao.save(flight);

		}

	}

}
