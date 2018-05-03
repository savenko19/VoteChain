package com.example.user.votechain.Blockchain;

import java.util.ArrayList;

public class VoteChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static Boolean isChainValid() {
        Block currentBlock;
        Block preBlock;

        for(int i = 0; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            preBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.print("Error!");
                return false;
            }

            if (!preBlock.hash.equals(currentBlock.preHash)) {
                System.out.print("Error!");
                return false;
            }
        }

        return true;
    }

}
