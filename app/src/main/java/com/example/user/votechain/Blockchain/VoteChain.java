package com.example.user.votechain.Blockchain;

import java.util.ArrayList;

public class VoteChain {
    public static ArrayList<Block>  chain = new ArrayList<>();

    public static boolean IsValidChain() {
        Block currentBlock;
        Block preBlock;

        for (int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            preBlock = chain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!preBlock.hash.equals(currentBlock.preHash)) {
                return false;
            }
        }

        System.out.println("Blockchain is valid");
        return true;
    }
}
