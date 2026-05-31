package com.hotel.booking.ifsp.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class ApiIntegrationTestBase {

    protected static final String ADMIN_EMAIL = "admin@hotel.com";
    protected static final String ADMIN_PASSWORD = "admin123";

    @LocalServerPort
    protected int port;

    @BeforeEach
    void configureRestAssured() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected String getAdminToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", ADMIN_EMAIL, "password", ADMIN_PASSWORD))
                .when()
                .post("/api/v1/authenticate")
                .then()
                .statusCode(200)
                .extract().path("token");
    }
}