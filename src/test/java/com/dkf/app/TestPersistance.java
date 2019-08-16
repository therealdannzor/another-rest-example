package com.dkf.app;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ConcurrentHashMap;
import com.dkf.app.persistance.DataStore;
import org.junit.Test;

public class TestPersistance {

    @Test
    public void testSimpleInitialiseAccount() {
        Integer expected = 0;

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();
        storage.AddUser("Alice");
        Integer actual = map.get("Alice").intValue();

        assertEquals(expected, actual);
    }

    @Test
    public void testSimpleInitialiseAccountWithBalance() {
        Integer expected = 2;

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();
        storage.AddUser("Alice");
        storage.SetUserBalance("Alice", 2);
        Integer actual = map.get("Alice");

        assertEquals(expected, actual);
    }

    @Test
    public void testTransferBetweenAccounts_HappyCase_ReturnOK() {
        Integer expected = 2;

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();
        storage.AddUser("Alice");
        storage.AddUser("Bob");
        storage.SetUserBalance("Alice", 1);
        storage.SetUserBalance("Bob", 1);
        storage.CashTransfer("Bob", "Alice", 1);
        Integer actual = map.get("Alice");

        assertEquals(expected, actual);
    }

    @Test
    public void testTransferBetweenAccounts_InsufficientFunds() {
        Integer expectedAliceBalance = 1;
        Integer expectedBobBalance = 0;

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();
        storage.AddUser("Alice");
        storage.AddUser("Bob");
        storage.SetUserBalance("Alice", 1);
        storage.CashTransfer("Alice", "Bob", 2);
        Integer actualAliceBalance = map.get("Alice");
        Integer actualBobBalance = map.get("Bob");

        assertEquals(expectedAliceBalance, actualAliceBalance);
        assertEquals(expectedBobBalance, actualBobBalance);
    }

    @Test
    public void testSingleTransferBetweenAccounts_SuccessTransfer() {
        Integer expectedAliceBalance = 3;
        Integer expectedBobBalance = 1;

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();
        storage.AddUser("Alice");
        storage.AddUser("Bob");
        storage.SetUserBalance("Alice", 2);
        storage.SetUserBalance("Bob", 2);
        storage.CashTransfer("Bob", "Alice", 1);
        Integer actualAliceBalance = map.get("Alice");
        Integer actualBobBalance = map.get("Bob");

        assertEquals(expectedAliceBalance, actualAliceBalance);
        assertEquals(expectedBobBalance, actualBobBalance);
    }

    @Test
    public void testMultipleTransferBetweenAccounts_SuccessTransfer() {
        Integer expectedAliceBalance = 4;
        Integer expectedBobBalance = 0;

        DataStore storage = new DataStore();
        ConcurrentHashMap<String, Integer> map = storage.returnHashmap();
        storage.AddUser("Alice");
        storage.AddUser("Bob");
        storage.SetUserBalance("Alice", 2);
        storage.SetUserBalance("Bob", 2);

        storage.CashTransfer("Bob", "Alice", 1); // Alice: 3, Bob: 1
        storage.CashTransfer("Alice", "Bob", 2); // Alice: 1, Bob: 3
        storage.CashTransfer("Bob", "Alice", 4); // invalid, Alice: 1, Bob: 3
        storage.CashTransfer("Alice", "Bob", 5); // invalid, Alice: 1, Bob: 3
        storage.CashTransfer("Bob", "Alice", 1); // Alice: 2, Bob: 2
        storage.CashTransfer("Bob", "Alice", 1); // Alice: 3, Bob: 1
        storage.CashTransfer("Alice", "Bob", 1); // Alice: 2, Bob: 2
        storage.CashTransfer("Bob", "Alice", 1); // Alice: 3, Bob: 1
        storage.CashTransfer("Bob", "Alice", 1); // Alice: 4, Bob: 0
        storage.CashTransfer("Bob", "Alice", 1); // Alice: 4, Bob: 0

        Integer actualAliceBalance = map.get("Alice");
        Integer actualBobBalance = map.get("Bob");

        assertEquals(expectedAliceBalance, actualAliceBalance);
        assertEquals(expectedBobBalance, actualBobBalance);
    }

}
