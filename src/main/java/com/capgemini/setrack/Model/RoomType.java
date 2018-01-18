package com.capgemini.setrack.model;

public class RoomType {

    private String type;
    private long id;

    public RoomType(){}

    public RoomType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
