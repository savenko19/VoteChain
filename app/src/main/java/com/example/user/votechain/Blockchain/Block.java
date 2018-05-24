package com.example.user.votechain.Blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String preHash;
    public ArrayList<Transaction> transactions;
    private long timeStamp;

    public Block(String preHash) {
        this.hash = calculateHash();
        this.preHash = preHash;

        this.timeStamp = new Date().getTime();
    }

    public void AddTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String calculateHash() {
        String calculatedhash = applySha256(preHash
                + Long.toString(timeStamp)
                + transactions);

        return calculatedhash;
    }

    public static String applySha256(String input){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            //Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
