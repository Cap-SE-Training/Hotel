package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomRepository {

    public List<Room> rooms = new ArrayList<Room>();
}
