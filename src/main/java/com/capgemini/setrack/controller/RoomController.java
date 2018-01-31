package com.capgemini.setrack.controller;

import com.capgemini.setrack.ValidationUtility;
import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.exception.NotFoundException;
import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
    public Room createRoom(@RequestBody Room room) throws InvalidModelException {
        room.validate();

        try {
            this.roomRepository.save(room);
            return room;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!");
        }
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Room editRoom(@RequestBody Room room) throws InvalidModelException {
        room.validate();

        try {
            this.roomRepository.save(room);
            return room;
        } catch(DataIntegrityViolationException e){
            throw ValidationUtility.getInvalidModelException(e);
        } catch(Exception e){
            throw new InvalidModelException("Something went wrong!");
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteRoom(@PathVariable long id) throws InvalidModelException, NotFoundException {
        try{
            this.roomRepository.delete(id);
        } catch(DataIntegrityViolationException e) {
            throw ValidationUtility.getInvalidModelException(e);
        } catch(EmptyResultDataAccessException e){
            throw new NotFoundException("There is no room with id " + id);
        }
    }

    @RequestMapping(value = "sizes", method = RequestMethod.GET)
    public Iterable<Integer> getAllRoomSizes() {
        return roomRepository.findDistinctRoomSizes();
    }
}
