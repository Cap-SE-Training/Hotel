package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.model.RoomType;
import com.capgemini.setrack.model.enums.RoomStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomRepository {
    private List<Room> rooms = new ArrayList<Room>();

    public RoomRepository() {
        //TODO: Remove this when we have a proper database
        RoomType roomType = new RoomType();
        roomType.setType("Luxe");

        Room room = new Room();
        room.setId(1);
        room.setName("The Batman Room");
        room.setPrice(300);
        room.setNumber("5");
        room.setRoomStatus(RoomStatus.AVAILABLE);
        room.setRoomType(roomType);
        room.setSize(2);
        this.rooms.add(room);

        room = new Room();
        room.setId(2);
        room.setName("The Superman Room");
        room.setPrice(300);
        room.setNumber("5");
        room.setRoomStatus(RoomStatus.AVAILABLE);
        room.setRoomType(roomType);
        room.setSize(2);
        this.rooms.add(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
