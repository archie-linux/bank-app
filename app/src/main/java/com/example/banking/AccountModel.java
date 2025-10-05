package com.example.banking;

public class AccountModel {
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
}

