package com.hotel.booking.ifsp.persistence;

import com.hotel.booking.ifsp.security.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
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
}