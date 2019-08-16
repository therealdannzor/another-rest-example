package com.dkf.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.*;

public class TestBalanceRoute {

    @Test
    public void testBasicHeartbeat() {
        RestAssured.given().when().get("/balance").then().statusCode(200);
    }

    @Test
    public void testRequestEmptyUserHeaderReturnError() {
        RestAssured.expect().body(containsString("Error: no user to check balance")).when().get("/balance");
    }

    @Test
    public void testRequestValidUserReturnBalance() {
        String expected = "\"Your balance: 5\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("user", "Alice");

        Response response = httpRequest.get("/balance");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testRequestUserReturnError() {
        String expected = "\"Error: user not found\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("user", "bob"); // Only 'Alice' and 'Bob' exists

        Response response = httpRequest.get("/balance");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testMultipleRequestsValidUserReturnBalance() {
        String expected = "\"Your balance: 5\"";
        RestAssured.baseURI = "http://localhost:8080";
        Integer numberOfThreads = 3;

        ExecutorService es = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            es.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        RequestSpecification httpRequest = RestAssured.given();
                        httpRequest.header("user", "Bob");

                        Response response = httpRequest.get("/balance");

                        assertEquals(expected, response.asString());
                    } catch (Exception e) {
                        System.out.printf("Not OK: ", e);
                    }
                }
            });
        }

        try {
            es.awaitTermination(20L, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            System.out.printf("Not OK: ", ie);
        }
    }
}