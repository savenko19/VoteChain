package com.example.user.votechain.Blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String preHash;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public int proof;
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

    public int CreatePoW(int lastProof, String preHash) {
        int proof = 0;
        while (!IsValidProof(lastProof, proof, preHash)) {
            proof++;
        }

        return proof;
    }

    private boolean IsValidProof(int lastProof, int proof, String preHash) {
        String result = applySha256(lastProof
        +proof
        +preHash);

        return result.startsWith("0000");
    }


}
