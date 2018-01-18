package com.capgemini.setrack.repository;

import com.capgemini.setrack.Model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomRepository {

    public List<Room> rooms = new ArrayList<Room>();
}
