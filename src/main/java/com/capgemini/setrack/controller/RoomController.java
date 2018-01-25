package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.model.RoomType;
import com.capgemini.setrack.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rooms/")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Room createRoom(@RequestBody Room room) {
        this.roomRepository.save(room);
        return room;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteRoom(@PathVariable long id) {
        for (Room room : this.roomRepository.findAll()) {
            if (room.getId() == id) {
                this.roomRepository.delete(room);
                break;
            }
        }
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public Iterable<Room> getAvailableRooms(
            @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
            @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate,
            @RequestParam("size") int size,
            @RequestParam("room_type_id") long room_type_id) {
        if(size > 0 && room_type_id > 0) {
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate, size, room_type_id);
        } else if (size > 0) {
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate, size);
        } else if (room_type_id > 0){
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate, room_type_id);
        } else {
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate);
        }
    }
}
