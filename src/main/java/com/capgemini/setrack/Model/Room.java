package com.capgemini.setrack.Model;

import com.capgemini.setrack.Model.Enums.RoomStatus;

public class Room {

    private RoomStatus roomStatus;
    private String number;
    private RoomType roomType;
    private int size;
    private long id;
    private double price;
    
    public Room(){}

    public Room(RoomStatus roomStatus, String number, RoomType roomType, int size, double price) {
        this.roomStatus = roomStatus;
        this.number = number;
        this.roomType = roomType;
        this.size = size;
        this.price = price;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public long getId() {
        return id;
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
