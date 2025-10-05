package com.example.banking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountModel implements Serializable {
    private String accountId;
    private String accountType;
    private String accountBalance;

    private String accountNumber;
    public AccountModel(String accountId, String accountType, String accountNumber, String accountBalance) {
        this.accountId = accountId;
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

    public String getAccountId() {
        return accountId;
    }
}

