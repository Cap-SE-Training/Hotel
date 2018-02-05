package com.capgemini.setrack.model;

import com.capgemini.setrack.model.enums.RoomStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table( name="Room", uniqueConstraints= {
        @UniqueConstraint(name = "UK_ROOM_NAME", columnNames = {"name"}),
        @UniqueConstraint(name = "UK_ROOM_NUMBER", columnNames = {"number"})
})
public class Room extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="room_type_id", foreignKey=@ForeignKey(name = "FK_ROOM_ROOMTYPE"))
    @NotNull(message="A room type is required!")
    private RoomType roomType;

    @NotNull(message="A room status is required!")
    private RoomStatus roomStatus;

    @NotNull(message="A name is required!")
    @Size(min=2, max=30, message="A name must be between 2 and 30 characters long!")
    private String name;

    @NotNull(message="A number is required!")
    @Size(min=1, message="A number is required")
    private String number;

    @NotNull(message="A size is required!")
    @Min(value=1, message="The size must be at least 1!")
    private int size;

    @NotNull(message="A price is required!")
    @Min(value=1L, message="The price must be at least 1!")
    private double price;

    @JsonIgnore
    @ManyToMany(mappedBy = "rooms", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Booking> bookings;

    public Room(){}

    public Room(String name, String number, RoomType roomType, int size, double price) {
        this.name = name;
        this.number = number;
        this.roomType = roomType;
        this.size = size;
        this.price = price;
        this.roomStatus = RoomStatus.AVAILABLE;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
