package com.example.user.votechain.Blockchain;

import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    public String transactionHash;
    public PublicKey sender;
    public PublicKey reciepient;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();


    public int value;
    public byte[] signature;

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, int value) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
    }
}
