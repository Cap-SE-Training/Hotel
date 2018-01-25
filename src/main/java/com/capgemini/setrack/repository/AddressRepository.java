package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {}