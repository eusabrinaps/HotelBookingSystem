package com.hotel.booking.ifsp.infrastructure.persistence;

import com.hotel.booking.ifsp.domain.guest.CPF;
import com.hotel.booking.ifsp.domain.guest.Guest;
import com.hotel.booking.ifsp.domain.guest.GuestId;
import com.hotel.booking.ifsp.domain.guest.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GuestRepositoryAdapter implements GuestRepository {

    private final JpaGuestRepositorySpring jpaRepository;

    @Override
    public Optional<Guest> findById(GuestId id) {
        return jpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Guest save(Guest guest) {
        GuestEntity entity = toEntity(guest);
        GuestEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    public List<Guest> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Optional<Guest> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf).map(this::toDomain);
    }

    private Guest toDomain(GuestEntity entity) {
        return new Guest(new GuestId(entity.getId()), entity.getName(), new CPF(entity.getCpf()));
    }

    private GuestEntity toEntity(Guest guest) {
        return GuestEntity.builder()
                .id(guest.getId().value())
                .name(guest.getName())
                .cpf(guest.getCpf().value())
                .build();
    }
}
