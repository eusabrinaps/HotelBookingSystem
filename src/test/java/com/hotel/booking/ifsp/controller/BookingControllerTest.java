package com.hotel.booking.ifsp.controller;

import com.hotel.booking.ifsp.infrastructure.persistence.JpaBookingRepositorySpring;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@DisplayName("BookingController API Tests")
class BookingControllerTest extends ApiIntegrationTestBase {
    private static final UUID GUEST_ID = UUID.fromString("d203ae32-5f90-4549-b53c-5b55764d05b5");
    private static final UUID UNKNOWN_GUEST_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNKNOWN_BOOKING_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Autowired
    private JpaBookingRepositorySpring bookingRepository;

    private final List<UUID> createdBookingIds = new ArrayList<>();

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAllById(createdBookingIds);
        createdBookingIds.clear();
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /bookings with valid token returns 200 and booking list")
    void shouldListBookingsAndReturnBookingPayload() {
        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].guestId", notNullValue())
                .body("[0].guestName", notNullValue())
                .body("[0].guestCpf", notNullValue())
                .body("[0].roomCategory", notNullValue())
                .body("[0].checkIn", notNullValue())
                .body("[0].checkOut", notNullValue())
                .body("[0].totalValue", notNullValue())
                .body("[0].status", notNullValue());
    }
}