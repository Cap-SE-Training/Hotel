package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Address;
import com.capgemini.setrack.model.Guest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestRepository {
    private List<Guest> guests = new ArrayList<Guest>();

    public GuestRepository() {
        Guest guest = new Guest();
        guest.setEmail("superman@gmail.com");
        guest.setFirstName("Clark");
        guest.setLastName("Kent");
        guest.setTelephoneNumber("0612345678");
        guest.setId(1);
        this.guests.add(guest);

        guest = new Guest();
        guest.setEmail("partytijger69@gmail.com");
        guest.setFirstName("Appie");
        guest.setLastName("Kap");
        guest.setTelephoneNumber("+31306893199");
        guest.setId(1337);
        this.guests.add(guest);
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }
}
