package com.hotel.booking.ifsp.controller;

import io.restassured.http.ContentType;
import org.apiguardian.api.API;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;


public class TransactionControllerTest extends ApiIntegrationTestBase {

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /hello with valid token returns 200 and user id")
    void HelloWithValidTokenReturns200AndUserId() {
        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when().get("/api/v1/hello")
                .then().statusCode(200)
                .body(notNullValue())
                .body(containsString("Hello: "));
    }

    @Test
    @Tag("ApiTest")
    @Tag("IntegrationTest")
    @DisplayName("GET /hello without token returns 401")
    void HelloWithoutTokenReturns401() {
        given()
                .when().get("/api/v1/hello")
                .then().statusCode(401);
    }

}
