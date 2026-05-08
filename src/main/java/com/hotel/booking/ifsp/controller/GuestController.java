package com.hotel.booking.ifsp.controller;

import com.hotel.booking.ifsp.domain.guest.CPF;
import com.hotel.booking.ifsp.domain.guest.Guest;
import com.hotel.booking.ifsp.domain.guest.GuestId;
import com.hotel.booking.ifsp.infrastructure.persistence.GuestRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestRepositoryAdapter guestRepository;

    public record GuestRequest(String name, String cpf) {}
    public record GuestResponse(UUID id, String name, String cpf) {}

    @GetMapping
    public List<GuestResponse> listAll() {
        return guestRepository.findAll().stream()
                .map(g -> new GuestResponse(g.getId().value(), g.getName(), g.getCpf().value()))
                .toList();
    }

    @PostMapping
    public ResponseEntity<GuestResponse> create(@RequestBody GuestRequest request) {
        Guest guest = new Guest(new GuestId(UUID.randomUUID()), request.name(), new CPF(request.cpf()));
        Guest saved = guestRepository.save(guest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GuestResponse(saved.getId().value(), saved.getName(), saved.getCpf().value()));
    }
}
