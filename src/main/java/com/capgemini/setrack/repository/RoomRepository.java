package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
    @Query("SELECT DISTINCT r.size FROM Room r ORDER BY r.size")
    Iterable<Integer> findDistinctRoomSizes();
}
