package com.hotel.booking.ifsp.persistence;

import com.hotel.booking.ifsp.security.user.JpaUserRepository;
import com.hotel.booking.ifsp.security.user.Role;
import com.hotel.booking.ifsp.security.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@DisplayName("User Persistence Tests")
class UserPersistenceTest extends PersistenceIntegrationTestBase {

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("findByEmail with existing email returns user")
    void FindByEmailWithExistingEmailReturnsUser() {
        // admin@hotel.com inserido pela migração
        var obtained = userRepository.findByEmail("admin@hotel.com");

        assertThat(obtained).isPresent();
        assertEquals("admin@hotel.com", obtained.get().getEmail());
        assertEquals("Admin", obtained.get().getName());
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("findByEmail with non-existing email returns empty")
    void FindByEmailWithNonExistingEmailReturnsEmpty() {
        var obtained = userRepository.findByEmail("ViriginiaNaCPIDasBets@test.com");

        assertThat(obtained).isEmpty();
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("save user with duplicate email throws DataIntegrityViolationException")
    void SaveUserWithDuplicateEmailThrowsDataIntegrityViolationException() {
        var duplicate = User.builder()
                .id(UUID.randomUUID())
                .name("Admin")
                .lastname("Duplicado")
                .email("admin@hotel.com")
                .password("CopaDoMundo")
                .role(Role.USER)
                .build();

        assertThatThrownBy(() -> userRepository.saveAndFlush(duplicate))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }
}