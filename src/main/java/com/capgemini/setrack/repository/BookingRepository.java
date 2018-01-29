package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {}
