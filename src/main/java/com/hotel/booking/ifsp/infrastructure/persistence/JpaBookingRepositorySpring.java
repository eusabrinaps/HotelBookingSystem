package com.hotel.booking.ifsp.infrastructure.persistence;

import com.hotel.booking.ifsp.domain.booking.BookingStatus;
import com.hotel.booking.ifsp.domain.room.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface JpaBookingRepositorySpring extends JpaRepository<BookingEntity, UUID> {

    List<BookingEntity> findAllByOrderByCheckInAsc();

    @Query("""
            SELECT COUNT(b) FROM BookingEntity b
            WHERE b.roomCategory = :category
              AND b.status NOT IN (:cancelled)
              AND (:excludeId IS NULL OR b.id <> :excludeId)
              AND b.checkIn < :checkOut
              AND b.checkOut > :checkIn
            """)
    long countOverlappingBookings(
            @Param("category") RoomCategory category,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("excludeId") UUID excludeId,
            @Param("cancelled") List<BookingStatus> cancelled
    );
}
