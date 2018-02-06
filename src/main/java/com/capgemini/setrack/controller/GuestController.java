package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.exception.NotFoundException;
import com.capgemini.setrack.model.Guest;
import com.capgemini.setrack.repository.GuestRepository;
import com.capgemini.setrack.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        guest.getAddress().validate();
        guest.validate();

        try{
            this.guestRepository.save(guest);
            return guest;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!"+e.getMessage().toString());
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Guest updateGuest(@RequestBody Guest guest) throws InvalidModelException {
        guest.getAddress().validate();
        guest.validate();

        try{
            this.guestRepository.save(guest);
            return guest;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!");
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteGuest(@PathVariable long id) throws InvalidModelException, NotFoundException {
        try{
            this.guestRepository.delete(id);
        } catch(DataIntegrityViolationException e) {
            throw ValidationUtility.getInvalidModelException(e);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("There is no guest with id " + id);
        }
    }
}

