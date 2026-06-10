package com.hotel.booking.ifsp.controller;

import com.hotel.booking.ifsp.domain.booking.BookingStatus;
import com.hotel.booking.ifsp.domain.room.RoomCategory;
import com.hotel.booking.ifsp.infrastructure.persistence.BookingEntity;
import com.hotel.booking.ifsp.infrastructure.persistence.JpaBookingRepositorySpring;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.*;

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

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /bookings/{id} with existing id returns 200 and booking payload")
    void shouldFindBookingByIdAndReturnBookingPayload() {
        UUID bookingId = createBooking(
                RoomCategory.STANDARD,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                BookingStatus.PENDING
        );

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("id", equalTo(bookingId.toString()))
                .body("guestId", equalTo(GUEST_ID.toString()))
                .body("guestName", notNullValue())
                .body("guestCpf", notNullValue())
                .body("roomCategory", equalTo("STANDARD"))
                .body("checkIn", equalTo(LocalDate.now().plusDays(10).toString()))
                .body("checkOut", equalTo(LocalDate.now().plusDays(12).toString()))
                .body("totalValue", notNullValue())
                .body("status", equalTo("PENDING"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /bookings/{id} with unknown id returns 404")
    void shouldReturnNotFoundWhenBookingDoesNotExist() {
        UUID unknownBookingId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings/{id}", unknownBookingId)
                .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /bookings with valid payload returns 201 and booking id")
    void shouldCreateBookingAndReturnBookingId() {
        Map<String, Object> body = Map.of(
                "guestId", GUEST_ID.toString(),
                "roomCategory", "STANDARD",
                "checkIn", LocalDate.now().plusDays(20).toString(),
                "checkOut", LocalDate.now().plusDays(22).toString()
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAdminToken())
                .body(body)
                .when()
                .post("/api/v1/bookings")
                .then()
                .statusCode(201)
                .body(notNullValue())
                .extract()
                .response();

        UUID bookingId = extractUuid(response);
        createdBookingIds.add(bookingId);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("PUT /bookings/{id} with valid payload returns 204 and updates booking")
    void shouldUpdateBookingAndReturnNoContent() {
        UUID bookingId = createBooking(
                RoomCategory.STANDARD,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                BookingStatus.PENDING
        );

        Map<String, Object> body = Map.of(
                "roomCategory", "DELUXE",
                "checkIn", LocalDate.now().plusDays(30).toString(),
                "checkOut", LocalDate.now().plusDays(33).toString()
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAdminToken())
                .body(body)
                .when()
                .put("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("roomCategory", equalTo("DELUXE"))
                .body("checkIn", equalTo(LocalDate.now().plusDays(30).toString()))
                .body("checkOut", equalTo(LocalDate.now().plusDays(33).toString()))
                .body("status", equalTo("PENDING"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("PATCH /bookings/{id}/cancel with pending booking returns 204 and cancels booking")
    void shouldCancelBookingAndReturnNoContent() {
        UUID bookingId = createBooking(
                RoomCategory.STANDARD,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                BookingStatus.PENDING
        );

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .patch("/api/v1/bookings/{id}/cancel", bookingId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("status", equalTo("CANCELLED"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("PATCH /bookings/{id}/checkin and checkout updates booking status")
    void shouldCheckinAndCheckoutBooking() {
        UUID bookingId = createBooking(
                RoomCategory.STANDARD,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(2),
                BookingStatus.PENDING
        );

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .patch("/api/v1/bookings/{id}/checkin", bookingId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("status", equalTo("CHECKED_IN"));

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .patch("/api/v1/bookings/{id}/checkout", bookingId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .get("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("status", equalTo("COMPLETED"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /bookings without token returns 401")
    void shouldReturn401WhenListingBookingsWithoutToken() {

        given()
                .when()
                .get("/api/v1/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /bookings without token returns 401")
    void shouldReturn401WhenCreatingBookingWithoutToken() {

        Map<String, Object> body = Map.of(
                "guestId", GUEST_ID.toString(),
                "roomCategory", "STANDARD",
                "checkIn", LocalDate.now().plusDays(20).toString(),
                "checkOut", LocalDate.now().plusDays(22).toString()
        );

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("PUT /bookings without token returns 401")
    void shouldReturn401WhenUpdatingBookingWithoutToken() {
        UUID bookingId = createBooking(
                RoomCategory.STANDARD,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                BookingStatus.PENDING
        );

        Map<String, Object> body = Map.of("roomCategory", "DELUXE", "checkIn", LocalDate.now().plusDays(30).toString(), "checkOut", LocalDate.now().plusDays(33).toString());

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/v1/bookings/{id}", bookingId)
                .then()
                .statusCode(401);
    }

    private UUID createBooking(
            RoomCategory roomCategory,
            LocalDate checkIn,
            LocalDate checkOut,
            BookingStatus status
    ) {
        UUID bookingId = UUID.randomUUID();

        long numberOfDays = ChronoUnit.DAYS.between(checkIn, checkOut);

        BookingEntity booking = BookingEntity.builder()
                .id(bookingId)
                .guestId(GUEST_ID)
                .roomCategory(roomCategory)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .totalValue(roomCategory.getDailyRate().multiply(BigDecimal.valueOf(numberOfDays)))
                .status(status)
                .build();

        bookingRepository.save(booking);
        createdBookingIds.add(bookingId);

        return bookingId;
    }

    private UUID extractUuid(Response response) {
        String rawBody = response.asString().replace("\"", "").trim();
        return UUID.fromString(rawBody);
    }
}