package com.cts.flight.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.flight.dao.FareDao;
import com.cts.flight.dao.FlightDao;
import com.cts.flight.entity.Fare;
import com.cts.flight.entity.Flight;

@Service
public class FareServiceImpl implements FareService {

	@Autowired
	private FlightDao flightDao;

	@Autowired
	private FareDao fareDao;

	@Override
	public Flight findByFlightNumberAndFlightDateAndOriginAndDestination(String flightNumber, LocalDate flightDate,
			String origin, String destination) {

		return flightDao.findByFlightNumberAndFlightDateAndOriginAndDestination(flightNumber, flightDate, origin,
				destination);
	}

	@Override
	public Fare getFareById(int id) {
		return fareDao.findById(id).orElse(null);
	}

}
