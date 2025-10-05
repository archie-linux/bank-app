package com.example.banking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountModel implements Serializable {
    private String accountType;
    private String accountBalance;

    private String accountNumber;
    public AccountModel(String accountType, String accountNumber, String accountBalance) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public static List<String> getAccountTypes(ArrayList<AccountModel> accounts) {
        List<String> accountTypes = new ArrayList<>();
        for (AccountModel account: accounts) {
            accountTypes.add(account.getAccountType());
        }
        return accountTypes;
    }
}

