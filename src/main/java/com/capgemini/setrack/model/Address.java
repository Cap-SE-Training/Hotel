package com.capgemini.setrack.model;

public class Address {

    private long id;
    private String street;
    private String housenumber;
    private String postalCode;
    private String city;
    private String country;

    public Address(long id, String street, String housenumber, String postalCode, String city, String country) {
        //id automatisch genereren
        this.street = street;
        this.housenumber = housenumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
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
}
