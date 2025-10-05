package com.example.banking;

public class TransactionModel {

    private final String transactionType;

    private final String transactionDesc;
    private final String timestamp;

    private final String amount;

    private final String currentBalance;

    public TransactionModel(String transactionType, String transactionDesc, String timestamp, String amount, String currentBalance) {
        this.transactionType = transactionType;
        this.transactionDesc = transactionDesc;
        this.timestamp = timestamp;
        System.out.println(transactionType);
        if (transactionType.equals("debit")) {
            this.amount = "- " +  amount;
        } else {
            this.amount = amount;
        }
        this.currentBalance = currentBalance;

    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }
}
