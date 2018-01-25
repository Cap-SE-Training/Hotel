package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Address;

import com.capgemini.setrack.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Address createAddress(@RequestBody Address address){
        this.addressRepository.save(address);
        return address;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteAddress(@PathVariable long id) {
        this.addressRepository.delete(id);
    }
}




