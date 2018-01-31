package com.capgemini.setrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
public class Address extends Model{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "address")
    @JsonIgnore
    private Guest guest;

    private String street;
    private String houseNumber;
    @Pattern(regexp="[0-9]{4}[A-Z]{2}",message="Postalcode should look like: '0000AA'")
    private String postalCode;
    @Pattern(regexp="[a-zA-Z ]*",message="A city name must consist of letters found in the latin alphabet")
    private String city;
    private String country;

    public Address() { }

    public Address(String street, String houseNumber, String postalCode, String city, String country) {
        //id automatisch genereren
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}