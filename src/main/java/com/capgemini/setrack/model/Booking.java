package com.capgemini.setrack.model;

import com.capgemini.setrack.model.enums.PaymentMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {

    private long roomId;
    private long guestId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime checkedIn;
    private LocalDateTime checkedOut;
    private LocalDateTime paid;
    private PaymentMethod paymentMethod;


    public Booking() {}

    public Booking(long roomId, long guestId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime checkedIn, PaymentMethod paymentMethod) {
        this.roomId = roomId; //roomId must be selected
        this.guestId = guestId;//guestId must be selected
        this.startDate = startDate;
        this.endDate = endDate;
        this.checkedIn = checkedIn;
        //checkedout can't be initialized from start since you can't checkout when a booking is created
        this.paymentMethod = paymentMethod;
        //paid is not required to be done at the start of a booking
    }

    public long getRoomId() {
        return roomId;
    }

    public long getGuestId() {
        return guestId;
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
