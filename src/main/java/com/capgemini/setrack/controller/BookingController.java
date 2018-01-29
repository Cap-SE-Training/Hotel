package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Booking;
import com.capgemini.setrack.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking/")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Booking> getAllBookings(){
        return this.bookingRepository.findAll();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Booking createBooking(@RequestBody Booking booking){
        this.bookingRepository.save(booking);
        return booking;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteBooking(@PathVariable long id) {
        this.bookingRepository.delete(id);
    }
}
