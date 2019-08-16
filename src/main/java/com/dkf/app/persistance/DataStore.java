package com.dkf.app.persistance;

import java.util.concurrent.ConcurrentHashMap;
import com.dkf.app.interfaces.TransferService;

// data persistance: in memory
public class DataStore implements TransferService {

    ConcurrentHashMap<String, Integer> chMap = new ConcurrentHashMap<String, Integer>();

    public ConcurrentHashMap<String, Integer> returnHashmap() {
        return chMap;
    }

    public void AddUser(String account) {
        chMap.putIfAbsent(account, 0);
    }

    public void SetUserBalance(String account, Integer amount) {
        chMap.put(account, amount);
    }

    public Integer GetUserBalance(String account) {
        return chMap.get("account");
    }

    public void CashTransfer(String fromAccount, String toAccount, Integer amountToSend) {
        Integer sendeeBalance = chMap.get(fromAccount);

        if (amountToSend > sendeeBalance) {
            return;
        }
        chMap.compute(toAccount, (key, val) -> val + amountToSend);
        chMap.compute(fromAccount, (key, val) -> val - amountToSend);
    }

}
