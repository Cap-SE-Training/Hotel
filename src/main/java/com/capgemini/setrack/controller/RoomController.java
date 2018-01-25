package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.exception.NotFoundException;
import com.capgemini.setrack.model.Model;
import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.repository.RoomRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public Room createRoom(@RequestBody Room room) throws InvalidModelException {
        room.validate();

        try {
            this.roomRepository.save(room);
            return room;
        } catch(DataIntegrityViolationException e){
            throw new InvalidModelException("This room already exists!");
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Room editRoom(@RequestBody Room room) {
        this.roomRepository.save(room);
        return room;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteRoom(@PathVariable long id) {
        this.roomRepository.delete(id);
    }

    @RequestMapping(value = "sizes", method = RequestMethod.GET)
    public Iterable<Integer> getAllRoomSizes() {
        return roomRepository.findDistinctRoomSizes();
    }
}
