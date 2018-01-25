package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Room;
import com.capgemini.setrack.model.RoomType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long>, JpaSpecificationExecutor {
    List<Room> findBySizeGreaterThanEqualAndRoomTypeOrderBySizeAsc(int size, RoomType roomType);

    @Query("SELECT * FROM Room r WHERE r.id NOT IN (\n" +
            "SELECT room_id FROM booking_room br INNER JOIN Booking b ON b.id = br.booking_id \n" +
            "WHERE b.startDate >= :startDate AND b.endDate >= :endDate\n" +
            ")")
    List<Room> findAvailableRoomsBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT * FROM Room r WHERE r.id NOT IN (\n" +
            "SELECT room_id FROM booking_room br INNER JOIN Booking b ON b.id = br.booking_id \n" +
            "WHERE b.startDate >= :startDate AND b.endDate >= :endDate AND r.size >= :size AND r.roomTypeId = :roomTypeId\n" +
            ")")
    List<Room> findAvailableRoomsBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                              @Param("size") int size, @Param("roomTypeId") long roomTypeId);

    @Query("SELECT * FROM Room r WHERE r.id NOT IN (\n" +
            "SELECT room_id FROM booking_room br INNER JOIN Booking b ON b.id = br.booking_id \n" +
            "WHERE b.startDate >= :startDate AND b.endDate >= :endDate AND r.size >= :size\n" +
            ")")
    List<Room> findAvailableRoomsBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                              @Param("size") int size);

    @Query("SELECT * FROM Room r WHERE r.id NOT IN (\n" +
            "SELECT room_id FROM booking_room br INNER JOIN Booking b ON b.id = br.booking_id \n" +
            "WHERE b.startDate >= :startDate AND b.endDate >= :endDate AND r.roomTypeId = :roomTypeId\n" +
            ")")
    List<Room> findAvailableRoomsBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                              @Param("roomTypeId") long roomTypeId);

}
