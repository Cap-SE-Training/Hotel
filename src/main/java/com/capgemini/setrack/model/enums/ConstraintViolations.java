package com.capgemini.setrack.model.enums;

public enum ConstraintViolations {
    ROOM_ROOMTYPE("An invalid room type has been chosen!"),
    ROOM_NUMBER("A room with this number already exists!"),
    ROOM_NAME("A room with this name already exists!"),
    BOOKING_ROOM("A booking has to have a room!"),
    BOOKING_GUEST("A booking has to be made by a guest!"),
    ROOMTYPE_TYPE("This room type already exists!");

    private String message;

    ConstraintViolations(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
