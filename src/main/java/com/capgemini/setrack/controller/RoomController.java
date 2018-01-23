package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
