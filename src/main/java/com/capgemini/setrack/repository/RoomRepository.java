package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.model.RoomType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long>, JpaSpecificationExecutor {
    List<Room> findBySizeGreaterThanEqualAndRoomTypeOrderBySizeAsc(int size, RoomType roomType);
}
