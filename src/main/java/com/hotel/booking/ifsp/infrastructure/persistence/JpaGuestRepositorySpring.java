package com.hotel.booking.ifsp.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaGuestRepositorySpring extends JpaRepository<GuestEntity, UUID> {
    Optional<GuestEntity> findByCpf(String cpf);
}
