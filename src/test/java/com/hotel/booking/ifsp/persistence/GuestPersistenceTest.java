package com.hotel.booking.ifsp.persistence;

import com.hotel.booking.ifsp.infrastructure.persistence.GuestEntity;
import com.hotel.booking.ifsp.infrastructure.persistence.JpaGuestRepositorySpring;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        assertThat(obtained).isPresent();
        assertEquals("Carlos Silva", obtained.get().getName());
        assertEquals("123.456.789-09", obtained.get().getCpf());
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("findByCpf with non-existing CPF returns empty")
    void FindByCpfWithNonExistingCpfReturnsEmpty() {
        var obtained = guestRepository.findByCpf("529.982.247-25");
        assertThat(obtained).isEmpty();
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("save guest with duplicate CPF throws DataIntegrityViolationException")
    void SaveGuestWithDuplicateCpfThrowsDataIntegrityViolationException() {
        var duplicate = GuestEntity.builder()
                .id(java.util.UUID.randomUUID())
                .name("Silvio Santos")
                .cpf("123.456.789-09")
                .build();

        assertThatThrownBy(() -> guestRepository.saveAndFlush(duplicate))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }
}