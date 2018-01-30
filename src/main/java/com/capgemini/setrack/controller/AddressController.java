package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.model.Address;
import com.capgemini.setrack.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address/")
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Address> getAddress(){
        return this.addressRepository.findAll();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Address createAddress(@RequestBody Address address) throws InvalidModelException {
        address.validate();

        try {
            this.addressRepository.save(address);
            return address;
        } catch(DataIntegrityViolationException e){
            throw new InvalidModelException("This address already exists!");
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Address editAddress(@RequestBody Address address) throws InvalidModelException {
        address.validate();

        this.addressRepository.save(address);
        return address;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteAddress(@PathVariable long id) {
        this.addressRepository.delete(id);
    }
}





