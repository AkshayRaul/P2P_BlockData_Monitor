package blokdata;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.*;
import java.util.*;


public class Blockchain {

    private String name;
    private String address;
    private List<Blockchain> peers;
    private List<Block> blockchain = new ArrayList<>();
    private final static Logger LOGGER = Logger.getLogger("Blockchain");
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
    private boolean listening = true;

    public Blockchain() {
    }

    Blockchain(final String name, final String address,  final Block root) {
        this.name = name;
        this.address = address;
        //this.peers = agents;
        blockchain.add(root);
    }

    public void addAgents(String agentId){

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    Block createBlock(String mode,String fileId,String peerId, String hash) {
        if (blockchain.isEmpty()) {
            return null;
        }

        Block previousBlock = getLatestBlock();
        if (previousBlock == null) {
            return null;
        }

        final int index = previousBlock.getIndex() + 1;
        final Block block = new Block(mode,index, hash, previousBlock.getHash(), name, peerId, fileId);
        LOGGER.info(String.format("%s created new block %s", name, block.toString()));
        return block;
    }

    void addBlock(Block block) {
        if (isBlockValid(block)) {
            blockchain.add(block);
        }
    }

     Block getLatestBlock() {
        if (blockchain.isEmpty()) {
            return null;
        }
        return blockchain.get(blockchain.size() - 1);
    }

    private boolean isBlockValid(final Block block) {
        final Block latestBlock = getLatestBlock();
        if (latestBlock == null) {
            return false;
        }
        final int expected = latestBlock.getIndex() + 1;
        if (block.getIndex() != expected) {
            System.out.println(String.format("Invalid index. Expected: %s Actual: %s", expected, block.getIndex()));
            return false;
        }
        if (!Objects.equals(block.getPreviousHash(), latestBlock.getHash())) {
            System.out.println("Unmatched hash code");
            return false;
        }
        return true;
    }

}
