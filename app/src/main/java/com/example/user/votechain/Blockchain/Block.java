package com.example.user.votechain.Blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

public class Block {
   private int index;
   private long timeStamp;
   private ArrayList<Transaction> transactions;
   private int proof;

    public Block(int index, ArrayList<Transaction> transactions, int proof, String preHash) {
        this.index = index;
        this.transactions = transactions;
        this.proof = proof;
        this.preHash = preHash;
        timeStamp = new Date().getTime();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getProof() {
        return proof;
    }

    public void setProof(int proof) {
        this.proof = proof;
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    private String preHash;

}
