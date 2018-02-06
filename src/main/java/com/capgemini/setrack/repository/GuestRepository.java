package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {}
