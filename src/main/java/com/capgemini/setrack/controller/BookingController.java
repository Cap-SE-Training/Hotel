package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.exception.NotFoundException;
import com.capgemini.setrack.model.Booking;
import com.capgemini.setrack.repository.BookingRepository;
import com.capgemini.setrack.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings/")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Booking> getAllBookings(){
        return this.bookingRepository.findAll();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Booking createBooking(@RequestBody Booking booking) throws InvalidModelException {
        booking.validate();

        try{
            this.bookingRepository.save(booking);
            return booking;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!");
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteBooking(@PathVariable long id) throws InvalidModelException, NotFoundException {
        try{
            this.bookingRepository.delete(id);
        } catch(DataIntegrityViolationException e) {
            throw ValidationUtility.getInvalidModelException(e);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("There is no booking with id " + id);
        }
    }
}
