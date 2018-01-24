package com.capgemini.setrack.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;

    @OneToOne(mappedBy="guest")
    private Address address;
    private String email;
    private String telephoneNumber;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "booking_guest", joinColumns = {
            @JoinColumn(name = "guestid", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "bookingid", referencedColumnName = "id") })
    private List<Booking> bookings;

    public Guest(){

    }

    public Guest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Guest(String firstName, String lastName, Address address, String email, String telephoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
