package real.health.Blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        // Add the genesis block
        chain.add(new Block("Genesis Block", "0"));
    }

    public void addBlock(String data) {
        Block newBlock = new Block(data, getLastBlock().getHash());
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.generateHash())) {
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public void printChain() {
        for (int i = 0; i < chain.size(); i++) {
            Block block = chain.get(i);
            System.out.printf("Block %d:\nData: %s\nPrevious Hash: %s\nHash: %s\n\n", i, block.getData(), block.getPreviousHash(), block.getHash());
        }
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public List<Block> getChain() {
        return chain;
    }
}
