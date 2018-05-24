package com.example.user.votechain.Blockchain;

public class Transaction {
    private long transactionId;
    private long userId;
    private long variantId;

    public Transaction(long userId, long variantId) {
        this.userId = userId;
        this.variantId = variantId;
    }
}

