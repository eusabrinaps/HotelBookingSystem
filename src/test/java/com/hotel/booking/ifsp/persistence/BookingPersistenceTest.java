package com.hotel.booking.ifsp.persistence;

import com.hotel.booking.ifsp.domain.booking.BookingStatus;
import com.hotel.booking.ifsp.domain.room.RoomCategory;
import com.hotel.booking.ifsp.infrastructure.persistence.BookingEntity;
import com.hotel.booking.ifsp.infrastructure.persistence.JpaBookingRepositorySpring;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("Booking Persistence Tests")
public class BookingPersistenceTest extends PersistenceIntegrationTestBase{
    private static final UUID GUEST_ID = UUID.fromString("d203ae32-5f90-4549-b53c-5b55764d05b5");

    @Autowired
    private JpaBookingRepositorySpring bookingRepository;

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("findAllByOrderByCheckInAsc returns bookings ordered by checkin")
    void shouldFindBookingsOrderedByCheckin() {
        BookingEntity laterBooking = createBooking(
                RoomCategory.STANDARD,
                LocalDate.of(2025, 1, 20),
                LocalDate.of(2025, 1, 22),
                BookingStatus.PENDING
        );

        BookingEntity earlierBooking = createBooking(
                RoomCategory.STANDARD,
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 12),
                BookingStatus.PENDING
        );

        BookingEntity middleBooking = createBooking(
                RoomCategory.STANDARD,
                LocalDate.of(2025, 1, 15),
                LocalDate.of(2025, 1, 17),
                BookingStatus.PENDING
        );

        bookingRepository.saveAll(List.of(laterBooking, earlierBooking, middleBooking));

        var result = bookingRepository.findAllByOrderByCheckInAsc();

        assertThat(result)
                .extracting(BookingEntity::getId)
                .startsWith(
                        earlierBooking.getId(),
                        middleBooking.getId(),
                        laterBooking.getId()
                );
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("countOverlappingBookings counts active overlapping bookings")
    void shouldCountOverlappingActiveBookings() {
        BookingEntity booking = createBooking(
                RoomCategory.STANDARD,
                LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 15),
                BookingStatus.PENDING
        );

        bookingRepository.save(booking);

        long result = countOverlappingBookings(
                RoomCategory.STANDARD,
                LocalDate.of(2025, 2, 12),
                LocalDate.of(2025, 2, 14),
                null
        );

        assertThat(result).isEqualTo(1L);
    }

    private long countOverlappingBookings(
            RoomCategory roomCategory,
            LocalDate checkIn,
            LocalDate checkOut,
            UUID excludeId
    ) {
        return bookingRepository.countOverlappingBookings(
                roomCategory,
                checkIn,
                checkOut,
                excludeId,
                List.of(BookingStatus.CANCELLED)
        );
    }

    private BookingEntity createBooking(
            RoomCategory roomCategory,
            LocalDate checkIn,
            LocalDate checkOut,
            BookingStatus status
    ) {
        long numberOfDays = ChronoUnit.DAYS.between(checkIn, checkOut);

        return BookingEntity.builder()
                .id(UUID.randomUUID())
                .guestId(GUEST_ID)
                .roomCategory(roomCategory)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .totalValue(roomCategory.getDailyRate().multiply(BigDecimal.valueOf(numberOfDays)))
                .status(status)
                .build();
    }
}
