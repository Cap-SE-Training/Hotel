package com.capgemini.setrack.model;
import com.capgemini.setrack.converter.LocalDateTimeDeserializer;
import com.capgemini.setrack.converter.LocalDateTimeSerializer;
import com.capgemini.setrack.model.enums.PaymentMethod;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime checkedIn;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime checkedOut;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime paid;

    private PaymentMethod paymentMethod;

    @ManyToMany(mappedBy="bookings")
    private List<Guest> guests;

    @ManyToMany(mappedBy="bookings")
    private List<Room> rooms;

    public Booking() {}

    public Booking(List<Room> rooms, List<Guest> guests, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime checkedIn, PaymentMethod paymentMethod) {
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

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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
