package com.cts.flight.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cts.flight.controller.Sender;
import com.cts.flight.dao.BookingDao;
import com.cts.flight.entity.BookingRecord;
import com.cts.flight.entity.Fare;
import com.cts.flight.entity.Flight;
import com.cts.flight.entity.Passenger;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private Sender sender;

	@Override
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private String fareurl = "http://localhost:8081/api/fare";
	private String searchUrl = "http://localhost:8082/api/search";

	@Override
	public BookingRecord bookFlight(String flightNumber, String origin, String destination, LocalDate flightDate,
			int numberofPassengers, Passenger passenger) {

		BookingRecord bookingRecord = null;
		Flight flight = restTemplate.getForObject(
				searchUrl + "/findFlight/" + flightNumber + "/" + flightDate + "/" + origin + "/" + destination,
				Flight.class);
		Fare fare = restTemplate.getForObject(
				fareurl + "/" + flightNumber + "/" + flightDate + "/" + origin + "/" + destination, Fare.class);

		if (flight.getInventory().getCount() > numberofPassengers) {

			bookingRecord = new BookingRecord();
			
		     System.out.println(">>>>>>>>>> Flight Info: "+flight.getFlightInfo());

			bookingRecord.setBookingDate(LocalDateTime.now());
			bookingRecord.setDestination(destination);
			bookingRecord.setFare(fare.getFare() * numberofPassengers);
			bookingRecord.setFlightDate(flightDate);
			bookingRecord.setFlightInfo(flight.getFlightInfo());
			bookingRecord.setFlightNumber(flightNumber);
			bookingRecord.setFlightTime(flight.getFlightTime());
			bookingRecord.setOrigin(origin);
			bookingRecord.setPassenger(passenger);
			bookingRecord.setStatus("CONFIRMED");

			System.out.println("Number of Passengers:>>>>  "+numberofPassengers);
			System.out.println("Number of CPassengers:>>>>  "+passenger.getCoPassengers().size());

			
			if (passenger.getCoPassengers().size() == numberofPassengers - 1) {
				
				bookingDao.save(bookingRecord);
				
			} else {
				System.out.println("Booking  can not be as passengers count is wringly provided...");
				return null;
			}

		} else {
			System.out.println("<<<<<<<<<<<< No Seats for booking >>>>>>>>>>>>");
			return null;
		}

		// Send Booked Inventory details to Search-Service
		Map<String, Object> bookingDetals = new HashMap<>();

		bookingDetals.put("FLIGHT_NUMBER", flight.getFlightNumber());
		bookingDetals.put("ORIGIN", flight.getOrigin());
		bookingDetals.put("DESTINATION", flight.getDestination());
		bookingDetals.put("FLIGHT_DATE", flight.getFlightDate());
		bookingDetals.put("NEW_INVENTORY", numberofPassengers);

		// Send Booking detials to email service
		Map<String, Object> emailDetails = new HashMap<>();

		emailDetails.put("FLIGHT_NUMBER", flight.getFlightNumber());
		emailDetails.put("ORIGIN", flight.getOrigin());
		emailDetails.put("DESTINATION", flight.getDestination());
		emailDetails.put("FLIGHT_DATE", flight.getFlightDate());
		emailDetails.put("DATE", flight.getFlightDate());
		emailDetails.put("TIME", flight.getFlightTime());
		emailDetails.put("NAME", passenger.getFirstName() + " " + passenger.getLastName());
		emailDetails.put("BOOKING_ID", bookingRecord.getBookingId());

		sender.sendBookingDetails(bookingDetals);
		sender.sendEmail(emailDetails);

		return bookingRecord;
	}

	@Override
	public void updateBookingStatus(String status, int bookingId) {
		BookingRecord bookingRecord = bookingDao.findById(bookingId).orElse(null);
		bookingRecord.setStatus(status);
		bookingDao.save(bookingRecord);
	}

	@Override
	public BookingRecord getBookingInfo(int bookingId) {
		return bookingDao.findById(bookingId).orElse(null);
	}

}
