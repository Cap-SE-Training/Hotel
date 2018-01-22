package com.capgemini.setrack.controller;

import com.capgemini.setrack.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking/")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;


    
}
