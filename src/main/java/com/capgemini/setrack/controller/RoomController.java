package com.capgemini.setrack.controller;

import com.capgemini.setrack.exception.InvalidModelException;
import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

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

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public Iterable<Room> getAvailableRooms(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate,
            @RequestParam(name="size", required=false) Integer size,
            @RequestParam(name="room_type_id", required=false) Long room_type_id) {

        if(size == null && room_type_id == null){
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate);
        } else if (size == null) {
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate, room_type_id);
        } else if (room_type_id == null){
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate, size);
        } else{
            return this.roomRepository.findAvailableRoomsBetweenDates(fromDate, toDate, size, room_type_id);
        }
    }
}
