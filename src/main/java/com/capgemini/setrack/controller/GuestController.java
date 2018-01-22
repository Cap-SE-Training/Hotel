package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Guest;
import com.capgemini.setrack.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guests/")
public class GuestController {

    @Autowired
    private GuestRepository guestRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Guest> getAllGuests(){
        return this.guestRepository.getGuests();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public void createGuest(@RequestBody Guest guest){
        this.guestRepository.getGuests().add(guest);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteGuest(@PathVariable long id) {
        for (Guest guest : this.guestRepository.getGuests()) {
            if (guest.getId() == id) {
                this.guestRepository.getGuests().remove(guest);
                break;
            }
        }


    }
}
