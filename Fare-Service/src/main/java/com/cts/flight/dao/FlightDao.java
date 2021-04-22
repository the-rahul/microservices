package com.cts.flight.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.flight.entity.Flight;

public interface FlightDao extends JpaRepository<Flight, Integer>{

	Flight findByFlightNumberAndFlightDateAndOriginAndDestination(String flightNumber,LocalDate flightDate,String origin,String destination);
	
}
