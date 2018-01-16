package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Guest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestRepository {

    public List<Guest> guests = new ArrayList<Guest>();
}
