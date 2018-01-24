package com.capgemini.setrack.model;

import com.capgemini.setrack.model.enums.RoomStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="room_type_id")
    @NotNull(message="A room type is required!")
    private RoomType roomType;

    @NotNull(message="A room status is required!")
    private RoomStatus roomStatus;

    @NotNull(message="A name is required!")
    @Size(min=2, max=30, message="A name must be between 2 and 30 characters long!")
    @Column(unique=true)
    private String name;

    @NotNull(message="A number is required!")
    @Size(min=1, message="A number is required")
    @Column(unique=true)
    private String number;

    @NotNull(message="A size is required!")
    @Min(value=1, message="The size must be at least 1!")
    private int size;

    @NotNull(message="A price is required!")
    @Min(value=1L, message="The size must be at least 1!")
    private double price;

    public Room(){}

    public Room(String name, String number, RoomType roomType, int size, double price) {
        this.name = name;
        this.number = number;
        this.roomType = roomType;
        this.size = size;
        this.price = price;
        this.roomStatus = RoomStatus.AVAILABLE;
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
