package com.dkf.app;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.notFound;
import static spark.Spark.internalServerError;
import static spark.Spark.port;
import spark.Request;
import spark.Response;

import java.util.concurrent.ConcurrentHashMap;
import com.dkf.app.persistance.DataStore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    private static ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) {
        System.out.println("--- Starting the client at port 8080 ---");
        port(8080);

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();

        // Initial allocation
        map.put("Alice", 5);
        map.put("Bob", 5);

        initRoutes(storage);
    }

    public static void initRoutes(DataStore storage) {
        initBalanceRoute(storage);
        initTransferRoute(storage);
        initDefaultMissingRoute();
        initInternalServerRoute();
    }

    public static void initBalanceRoute(DataStore storage) {
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();

        get("/balance", (Request req, Response resp) -> {
            String ofAccount = req.headers("user");

            if (ofAccount == null) {
                return om.writeValueAsString("Error: no user to check balance");
            } else if (!map.containsKey(ofAccount)) {
                return om.writeValueAsString("Error: user not found");
            }

            Integer balance = map.get(ofAccount);
            return om.writeValueAsString("Your balance: " + Integer.toString(balance));
        });
    }

    private static void initTransferRoute(DataStore storage) {
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();

        post("/transfer/:toName", (Request req, Response resp) -> {
            String fromAccount = req.headers("from");
            String toAccount = req.params(":toName");

            if (fromAccount == null || toAccount == null) {
                return om.writeValueAsString("Error: missing sender and/or recipient");
            } else if (!map.containsKey(fromAccount) || !map.containsKey(toAccount)) {
                return om.writeValueAsString("Error: invalid sender or receiver");
            }

            Integer senderBalance = map.get(fromAccount);
            String amountStr = req.headers("amount");

            if (amountStr == null) {
                return om.writeValueAsString("Error: missing amount to send");
            }

            Integer amount = Integer.parseInt(amountStr);
            if (amount > senderBalance) {
                return om.writeValueAsString("Error: not enough funds");
            } else if (amount < 1) {
                return om.writeValueAsString("Error: must send positive integers");
            }

            storage.CashTransfer(fromAccount, toAccount, amount);
            Integer newSenderBalance = map.get(fromAccount);
            String balanceStr = Integer.toString(newSenderBalance);

            return om.writeValueAsString("Sent: " + amountStr + ", new balance: " + balanceStr);
        });
    }

    private static void initDefaultMissingRoute() {
        notFound((Request request, Response response) -> {
            return om.writeValueAsString("Error: 404 page not found");
        });
    }

    private static void initInternalServerRoute() {
        internalServerError((Request request, Response response) -> {
            return om.writeValueAsString("Error: internal system error");
        });
    }

}