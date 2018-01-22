package com.capgemini.setrack.controller;

import com.capgemini.setrack.model.RoomType;
import com.capgemini.setrack.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room_types")
public class RoomTypeController {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<RoomType> getAllRoomTypes() {
        return this.roomTypeRepository.findAll();
    }
}
