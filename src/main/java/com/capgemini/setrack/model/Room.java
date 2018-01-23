package com.capgemini.setrack.model;

import com.capgemini.setrack.model.enums.RoomStatus;

import javax.persistence.*;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="room_type_id")
    private RoomType roomType;

    private RoomStatus roomStatus;
    private String name;
    private String number;
    private int size;
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
