package com.hotel.booking.ifsp.infrastructure.persistence;

import com.hotel.booking.ifsp.domain.booking.Booking;
import com.hotel.booking.ifsp.domain.booking.BookingId;
import com.hotel.booking.ifsp.domain.booking.BookingRepository;
import com.hotel.booking.ifsp.domain.booking.Period;
import com.hotel.booking.ifsp.domain.booking.BookingStatus;
import com.hotel.booking.ifsp.config.RoomInventoryProperties;
import com.hotel.booking.ifsp.domain.guest.GuestId;
import com.hotel.booking.ifsp.domain.room.RoomCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryAdapter implements BookingRepository {

    private final JpaBookingRepositorySpring jpaRepository;
    private final RoomInventoryProperties roomInventory;

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = toEntity(booking);
        BookingEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public Optional<Booking> findById(BookingId id) {
        return jpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional
    public boolean isRoomAvailable(RoomCategory roomCategory, Period period, BookingId excludeBookingId) {
        long occupied = jpaRepository.countOverlappingBookings(
                roomCategory,
                period.checkIn(),
                period.checkOut(),
                excludeBookingId != null ? excludeBookingId.value() : null,
                List.of(BookingStatus.CANCELLED)
        );
        return occupied < roomInventory.getCountFor(roomCategory);
    }

    @Transactional
    public List<Booking> findAll() {
        return jpaRepository.findAllByOrderByCheckInAsc()
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Booking toDomain(BookingEntity e) {
        return Booking.reconstitute(
                new BookingId(e.getId()),
                new GuestId(e.getGuestId()),
                e.getRoomCategory(),
                new Period(e.getCheckIn(), e.getCheckOut()),
                e.getTotalValue(),
                e.getStatus()
        );
    }

    private BookingEntity toEntity(Booking b) {
        return BookingEntity.builder()
                .id(b.getId().value())
                .guestId(b.getGuestId().value())
                .roomCategory(b.getRoomCategory())
                .checkIn(b.getPeriod().checkIn())
                .checkOut(b.getPeriod().checkOut())
                .totalValue(b.getTotalValue())
                .status(b.getStatus())
                .build();
    }
}
