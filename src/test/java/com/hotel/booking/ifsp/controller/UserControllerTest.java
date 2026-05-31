package com.hotel.booking.ifsp.controller;

import com.hotel.booking.ifsp.security.user.JpaUserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Tag("ApiTest")
@Tag("IntegrationTest")
@DisplayName("UserController API Tests")
class UserControllerTest extends ApiIntegrationTestBase {

    private static final String TEST_EMAIL = "test.register@example.com";

    @Autowired
    private JpaUserRepository userRepository;

    @BeforeEach
    void cleanTestUsers() {
        userRepository.findByEmail(TEST_EMAIL).ifPresent(userRepository::delete);
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register with valid data returns 201 and user id")
    void RegisterWithValidDataReturns201AndUserId() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "Test",
                        "lastname", "User",
                        "email", TEST_EMAIL,
                        "password", "Senha@123"
                ))
                .when().post("/api/v1/register")
                .then().statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /register with duplicate email returns 409")
    void RegisterWithDuplicateEmailReturns409() {
        // admin@hotel.com existe no banco pela migração
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "Admin",
                        "lastname", "Dup",
                        "email", ADMIN_EMAIL,
                        "password", "qualquerUma"
                ))
                .when().post("/api/v1/register")
                .then().statusCode(409)
                .body("message", notNullValue());
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("POST /authenticate with valid credentials returns 200 and JWT token")
    void AuthenticateWithValidCredentialsReturns200AndToken() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", ADMIN_EMAIL, "password", ADMIN_PASSWORD))
                .when().post("/api/v1/authenticate")
                .then().statusCode(200)
                .body("token", notNullValue());
    }
}