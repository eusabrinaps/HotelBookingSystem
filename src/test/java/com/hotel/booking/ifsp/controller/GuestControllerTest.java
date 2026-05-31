package com.hotel.booking.ifsp.controller;

import com.hotel.booking.ifsp.infrastructure.persistence.JpaGuestRepositorySpring;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("GuestController API Tests")
class GuestControllerTest extends ApiIntegrationTestBase {
    @Autowired
    private JpaGuestRepositorySpring guestRepository;

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /guests with valid token returns 200 and guest list")
    void ListGuestsWithValidTokenReturns200AndGuestList() {
        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when().get("/api/v1/guests")
                .then().statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].cpf", notNullValue());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /guests without token returns 401")
    void ListGuestsWithoutTokenReturns401() {
        given()
                .when().get("/api/v1/guests")
                .then().statusCode(401);
    }
}