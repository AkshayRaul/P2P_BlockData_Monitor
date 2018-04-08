package blokdata;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;



public class BlockchainServer {

    private List<Blockchain> agents = new ArrayList<Blockchain>();
    private static final Block root = new Block("ROOT",0, "HASH","ROOT_HASH", "ROOT","ROOT_ID","ROOT_PEER");
    private final static Logger LOGGER = Logger.getLogger("BlockchainServer");

    public Blockchain addAgent(String name) {
        Blockchain a = new Blockchain(name, "localhost", root);
        agents.add(a);
        return a;
    }
    public Blockchain addAgent(String name,String ipAddress) {
        Blockchain a = new Blockchain(name, ipAddress, root);
        agents.add(a);
        return a;
    }
    public Blockchain getAgent(String name) {
        for (Blockchain a : agents) {
            if (a.getName().compareTo(name)==0) {
                LOGGER.info("TRUE");
                return a;
            }
        }
        return null;
    }

    public List<Blockchain> getAllAgents() {
        return agents;
    }

    public void deleteAgent(String name) {
        final Blockchain a = getAgent(name);
        if (a != null) {
            agents.remove(a);
        }
    }

    public List<Block> getAgentBlockchain(String name) {
        final Blockchain blockchain = getAgent(name);
        if (blockchain != null) {
            return blockchain.getBlockchain();
        }
        return null;
    }

    public void deleteAllAgents() {
        agents.clear();
    }

    public Block createBlock(final String mode,final String name,final String fileId,final String peerId,final String hash) {
        final Blockchain blockchain = getAgent(name);
        if (blockchain != null) {
            return blockchain.createBlock(mode,fileId,peerId,hash);
        }
        return null;
    }
}
