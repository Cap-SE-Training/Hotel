package com.capgemini.setrack.model;

import com.capgemini.setrack.model.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
public class Booking extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message="A starting date is required!")
    private LocalDateTime startDate;

    @NotNull(message="An end date is required!")
    private LocalDateTime endDate;

    private LocalDateTime checkedIn;
    private LocalDateTime checkedOut;
    private LocalDateTime paid;

    private PaymentMethod paymentMethod;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "booking_guest", joinColumns = {
            @JoinColumn(name = "booking_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "guest_id", referencedColumnName = "id") })
    private Set<Guest> guests;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "booking_room", joinColumns = {
            @JoinColumn(name = "booking_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "room_id", referencedColumnName = "id") })
    private Set<Room> rooms;

    public Booking() {}

    public Booking(Set<Room> rooms, Set<Guest> guests, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime checkedIn, PaymentMethod paymentMethod) {
        this.rooms = rooms; //rooms must be selected
        this.guests = guests;//guests must be selected
        this.startDate = startDate;
        this.endDate = endDate;
        this.checkedIn = checkedIn;
        //checkedout can't be initialized from start since you can't checkout when a booking is created
        this.paymentMethod = paymentMethod;
        //paid is not required to be done at the start of a booking
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Guest> getGuests() {
        return guests;
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public void addGuest(Guest guest) {
        this.guests.add(guest);
        guest.getBookings().add(this);
    }

    public void removeGuest(Guest guest) {
        guests.remove(guest);
        guest.getBookings().remove(this);
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
        room.getBookings().add(this);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.getBookings().remove(this);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @JsonGetter("startDate")
    public long getJsonStartDate() {
        return startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @JsonSetter("startDate")
    public void setJsonStartDate(long startDate) {
        this.startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.systemDefault());
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @JsonGetter("endDate")
    public long getJsonEndDate() {
        return endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @JsonSetter("endDate")
    public void setJsonEndDate(long endDate) {
        this.endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneId.systemDefault());
    }

    public LocalDateTime getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(LocalDateTime checkedIn) {
        this.checkedIn = checkedIn;
    }

    public LocalDateTime getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(LocalDateTime checkedOut) {
        this.checkedOut = checkedOut;
    }

    public LocalDateTime getPaid() {
        return paid;
    }

    public void setPaid(LocalDateTime paid) {
        this.paid = paid;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
