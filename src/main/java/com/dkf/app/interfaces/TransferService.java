package com.dkf.app.interfaces;

public interface TransferService {

    // Only supports transactions in the UK
    public String GetCurrency = "GBP";

    // Create a new user
    void AddUser(String name);

    // Create user and print some money
    void SetUserBalance(String account, Integer amount);

    // Simple cash transfer
    void CashTransfer(String fromAccount, String toAccount, Integer amount);
}