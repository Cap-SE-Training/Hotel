package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepository {

    private List<Booking> bookings = new ArrayList<Booking>();

    public BookingRepository() {
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
