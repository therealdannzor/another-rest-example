package com.dkf.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestTransferRoute {

    @Test
    public void testTransferMissingBothSenderRecipientReturnError() {
        String expected = "\"Error: missing sender and/or recipient\"";

        RestAssured.baseURI = "http://localhost:8080";

        Response response = RestAssured.given().post("/transfer/Bob");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferMissingOnlySenderReturnError() {
        String expected = "\"Error: missing sender and/or recipient\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("amount", "2");

        Response response = httpRequest.post("/transfer/Bob");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferMissingOnlyRecipientReturnError() {
        String expected = "\"Error: 404 page not found\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("amount", "2").header("from", "Alice");

        Response response = httpRequest.post("/transfer");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferInvalidSenderReturnError() {
        String expected = "\"Error: invalid sender or receiver\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("from", "Älice");

        Response response = httpRequest.post("/transfer/Bob");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferInvalidRecipientReturnError() {
        String expected = "\"Error: invalid sender or receiver\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("from", "Bob");

        Response response = httpRequest.post("/transfer/Älice");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferMissingAmountReturnError() {
        String expected = "\"Error: missing amount to send\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("from", "Bob");

        Response response = httpRequest.post("/transfer/Alice");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferMoreThanBalanceReturnError() {
        String expected = "\"Error: not enough funds\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("from", "Bob").header("amount", "6");

        Response response = httpRequest.post("/transfer/Alice");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testTransferNegativeFundsReturnError() {
        String expected = "\"Error: must send positive integers\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("from", "Bob").header("amount", "-2");

        Response response = httpRequest.post("/transfer/Alice");

        assertEquals(expected, response.asString());
    }

    @Test
    public void testSimpleTransferReturnNewBalance() {
        String expected = "\"Sent: 1, new balance: 4\"";

        RestAssured.baseURI = "http://localhost:8080";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("from", "Alice").header("amount", "1");

        Response response = httpRequest.post("/transfer/Bob");

        assertEquals(expected, response.asString());
    }
}