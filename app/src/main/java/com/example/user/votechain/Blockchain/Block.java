package com.example.user.votechain.Blockchain;

import java.security.MessageDigest;
import java.util.Date;

public class Block {
    public String hash;
    public String preHash;
    private String data;
    private long timeStamp;

    public Block(String date, String previousHash) {
        this.data = date;
        this.preHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String calculatedHash = Sha256(preHash + Long.toString(timeStamp) + data);
        return calculatedHash;
    }

    public static String Sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
