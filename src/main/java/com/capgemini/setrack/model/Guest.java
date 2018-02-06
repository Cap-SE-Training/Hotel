package com.capgemini.setrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name="Guest", uniqueConstraints= {
        @UniqueConstraint(name = "UK_GUEST", columnNames = {"documentNumber"})
})
public class Guest extends Model{


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    //document toevoegen

    @NotNull(message = "First name is required!")
    @Pattern(regexp="[a-zA-Z ]*",message="A name must consist of letters found in the latin alphabet")
    @Size(min=2, max=30, message="A name must be between 2 and 30 characters long!")
    private String firstName;

    @NotNull(message = "Last name is required!")
    @Pattern(regexp="[a-zA-Z ]*",message="A name must consist of letters found in the latin alphabet")
    @Size(min=2, max=30, message="A name must be between 2 and 30 characters long!")
    private String lastName;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="address_id", foreignKey=@ForeignKey(name = "FK_GUEST_ADDRESS"))
    @NotNull(message = "Address is required!")
    private Address address;

    @Size(min = 1, max = 20)
    private String documentNumber;

//    @Size(min = 5, max = 100)
    @Pattern(regexp="^$|(?:[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message="Email is not valid!")
//    @NotNull(message="Email is required!")
    private String email;

    @Size(min = 10, max = 15)
    private String telephoneNumber;

    @JsonIgnore
    @ManyToMany(mappedBy = "guests", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Booking> bookings;

    public Guest(){ }

    public Guest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Guest(String firstName, String lastName, Address address, String email, String telephoneNumber, String documentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.documentNumber = documentNumber;
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

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getDocumentNumber() {return documentNumber;}

    public void setDocumentNumber(String documentNumber) {this.documentNumber = documentNumber;}
}

