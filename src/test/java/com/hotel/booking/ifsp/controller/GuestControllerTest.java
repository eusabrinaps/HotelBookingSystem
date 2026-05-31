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

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /guests with valid data returns 201 and guest body")
    void CreateGuestWithValidDataReturns201AndGuestBody() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAdminToken())
                .body(Map.of("name", "Linus Torvalds", "cpf", "529.982.247-25"))
                .when().post("/api/v1/guests")
                .then().statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Linus Torvalds"))
                .body("cpf", equalTo("529.982.247-25"));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /guests with invalid CPF returns 400")
    void CreateGuestWithInvalidCpfReturns400() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAdminToken())
                .body(Map.of("name", "Teste", "cpf", "123.456.789-00"))
                .when().post("/api/v1/guests")
                .then().statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /guests with blank name returns 400")
    void CreateGuestWithBlankNameReturns400() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAdminToken())
                .body(Map.of("name", " ", "cpf", "529.982.247-25"))
                .when().post("/api/v1/guests")
                .then().statusCode(400);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /guests with duplicate CPF returns 409") // TODO: Abrir issue como bug
    void CreateGuestWithDuplicateCpfReturns409() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAdminToken())
                .body(Map.of("name", "Roberto Carlos", "cpf", "123.456.789-09"))
                .when().post("/api/v1/guests")
                .then().statusCode(409);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /guests without token returns 401")
    void CreateGuestWithoutTokenReturns401() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Roberto Carlos", "cpf", "529.982.247-25"))
                .when().post("/api/v1/guests")
                .then().statusCode(401);
    }
}