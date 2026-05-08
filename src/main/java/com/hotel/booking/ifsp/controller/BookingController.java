package com.hotel.booking.ifsp.controller;

import com.hotel.booking.ifsp.application.booking.*;
import com.hotel.booking.ifsp.domain.booking.BookingId;
import com.hotel.booking.ifsp.domain.booking.Period;
import com.hotel.booking.ifsp.domain.guest.GuestId;
import com.hotel.booking.ifsp.domain.room.RoomCategory;
import com.hotel.booking.ifsp.infrastructure.persistence.BookingRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingQueryService bookingQueryService;
    private final BookingUpdateService bookingUpdateService;
    private final BookingCancelService bookingCancelService;
    private final BookingCheckInOutService bookingCheckInOutService;
    private final BookingRepositoryAdapter bookingRepository;

    public record CreateBookingRequest(UUID guestId, String roomCategory, LocalDate checkIn, LocalDate checkOut) {}
    public record UpdateBookingRequest(String roomCategory, LocalDate checkIn, LocalDate checkOut) {}
    public record BookingResponse(
            UUID id, UUID guestId, String guestName, String guestCpf,
            String roomCategory, LocalDate checkIn, LocalDate checkOut,
            BigDecimal totalValue, String status) {}

    @GetMapping
    public List<BookingResponse> listAll() {
        return bookingRepository.findAll().stream()
                .map(b -> {
                    try {
                        BookingDetails d = bookingQueryService.findBooking(b.getId());
                        return new BookingResponse(
                                d.bookingId().value(), b.getGuestId().value(),
                                d.guestName(), d.guestCpf(),
                                d.roomCategory().name(),
                                d.period().checkIn(), d.period().checkOut(),
                                d.totalValue(), d.status().name()
                        );
                    } catch (Exception e) {
                        return new BookingResponse(
                                b.getId().value(), b.getGuestId().value(),
                                "Unknown", "",
                                b.getRoomCategory().name(),
                                b.getPeriod().checkIn(), b.getPeriod().checkOut(),
                                b.getTotalValue(), b.getStatus().name()
                        );
                    }
                }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable UUID id) {
        BookingDetails d = bookingQueryService.findBooking(new BookingId(id));
        var b = bookingRepository.findById(new BookingId(id)).orElseThrow();
        return ResponseEntity.ok(new BookingResponse(
                d.bookingId().value(), b.getGuestId().value(),
                d.guestName(), d.guestCpf(),
                d.roomCategory().name(),
                d.period().checkIn(), d.period().checkOut(),
                d.totalValue(), d.status().name()
        ));
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody CreateBookingRequest request) {
        Period period = new Period(request.checkIn(), request.checkOut());
        BookingId id = bookingService.registerBooking(
                new GuestId(request.guestId()),
                RoomCategory.valueOf(request.roomCategory()),
                period
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(id.value());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody UpdateBookingRequest request) {
        Period period = new Period(request.checkIn(), request.checkOut());
        bookingUpdateService.updateBooking(
                new BookingId(id),
                RoomCategory.valueOf(request.roomCategory()),
                period
        );
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        bookingCancelService.cancelBooking(new BookingId(id));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<Void> checkIn(@PathVariable UUID id) {
        bookingCheckInOutService.checkIn(new BookingId(id));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<Void> checkOut(@PathVariable UUID id) {
        bookingCheckInOutService.checkOut(new BookingId(id));
        return ResponseEntity.noContent().build();
    }
}
