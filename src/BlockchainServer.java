
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;



public class BlockchainServer {

    private List<Blockchain> agents = new ArrayList<>();
    private static final Block root = new Block(0, "ROOT_HASH", "ROOT");
    private final static Logger LOGGER = Logger.getLogger("BlockchainServer");

    public Blockchain addAgent(String name) {
        Blockchain a = new Blockchain(name, "localhost", root, agents);
        agents.add(a);
        return a;
    }

    public Blockchain getAgent(String name) {
        for (Blockchain a : agents) {
            if (a.getName().equals(name)) {
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

    public Block createBlock(final String name) {
        final Blockchain blockchain = getAgent(name);
        if (blockchain != null) {
            return blockchain.createBlock();
        }
        return null;
    }
}
