package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.model.Guest;
import com.capgemini.setrack.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guests/")
public class GuestController {
    @Autowired
    private GuestRepository guestRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Guest> getAllGuests(){
        return this.guestRepository.findAll();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Guest createGuest(@RequestBody Guest guest) throws InvalidModelException {
        guest.validate();

        try {
            this.guestRepository.save(guest);
            return guest;
        } catch(DataIntegrityViolationException e){
            throw new InvalidModelException("This guest already exists!");
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Guest updateGuest(@RequestBody Guest guest) throws InvalidModelException {
        guest.validate();

        //check if guest already exists
        this.guestRepository.save(guest);
        return guest;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteGuest(@PathVariable long id) {
        this.guestRepository.delete(id);
    }
}

