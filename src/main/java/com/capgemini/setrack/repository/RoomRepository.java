package com.capgemini.setrack.repository;

import com.capgemini.setrack.model.Room;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RoomRepository extends CrudRepository<Room, Long>, JpaSpecificationExecutor {
    @Query("SELECT r1 FROM Room r1 WHERE r1.id NOT IN " +
                "(SELECT r.id FROM Room r INNER JOIN r.bookings b where " +
                "(b.startDate BETWEEN :startDate AND :endDate) OR (b.endDate BETWEEN :startDate AND :endDate))")
    Iterable<Room> findAvailableRoomsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r1 FROM Room r1 WHERE r1.id NOT IN " +
                "(SELECT r.id FROM Room r INNER JOIN r.bookings b where " +
                "(b.startDate BETWEEN :startDate AND :endDate) OR (b.endDate BETWEEN :startDate AND :endDate)) " +
            "AND r1.size >= :size")
    Iterable<Room> findAvailableRoomsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                                  @Param("size") int size);

    @Query("SELECT r1 FROM Room r1 INNER JOIN r1.roomType t WHERE r1.id NOT IN " +
                "(SELECT r.id FROM Room r INNER JOIN r.bookings b where " +
                "(b.startDate BETWEEN :startDate AND :endDate) OR (b.endDate BETWEEN :startDate AND :endDate)) " +
            "AND t.id = :room_type_id")
    Iterable<Room> findAvailableRoomsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                                  @Param("room_type_id") long room_type_id);

    @Query("SELECT r1 FROM Room r1 INNER JOIN r1.roomType t WHERE r1.id NOT IN " +
                "(SELECT r.id FROM Room r INNER JOIN r.bookings b where " +
                "(b.startDate BETWEEN :startDate AND :endDate) OR (b.endDate BETWEEN :startDate AND :endDate)) " +
            "AND t.id = :room_type_id AND r1.size >= :size")
    Iterable<Room> findAvailableRoomsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                                  @Param("size") int size, @Param("room_type_id") long room_type_id);

    @Query("SELECT DISTINCT r.size FROM Room r ORDER BY r.size")
    Iterable<Integer> findDistinctRoomSizes();
}
