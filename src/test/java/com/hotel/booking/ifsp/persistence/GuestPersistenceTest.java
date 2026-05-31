package com.hotel.booking.ifsp.persistence;

import com.hotel.booking.ifsp.infrastructure.persistence.JpaGuestRepositorySpring;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("Guest Persistence Tests")
class GuestPersistenceTest extends PersistenceIntegrationTestBase {

    @Autowired
    private JpaGuestRepositorySpring guestRepository;

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("findByCpf with existing CPF returns guest")
    void FindByCpfWithExistingCpfReturnsGuest() {
        var obtained = guestRepository.findByCpf("123.456.789-09");

        assertTrue(obtained.isPresent());
        assertEquals("Carlos Silva", obtained.get().getName());
        assertEquals("123.456.789-09", obtained.get().getCpf());
    }
}