package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.exception.NotFoundException;
import com.capgemini.setrack.model.Address;
import com.capgemini.setrack.repository.AddressRepository;
import com.capgemini.setrack.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

        try{
            this.addressRepository.save(address);
            return address;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!");
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Address editAddress(@RequestBody Address address) throws InvalidModelException {
        address.validate();

        try{
            this.addressRepository.save(address);
            return address;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!");
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteAddress(@PathVariable long id) throws InvalidModelException, NotFoundException {
        try{
            this.addressRepository.delete(id);
        } catch(DataIntegrityViolationException e) {
            throw ValidationUtility.getInvalidModelException(e);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("There is no guest with id " + id);
        }
    }
}





