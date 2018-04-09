package blokdata;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mode;
    private int index;
    private Long timestamp;
    private String hash;
    private String previousHash;
    private String creator;
    private String peerId;
    private String fileId;

    // for jackson
    public Block() {
    }
    public static void test(){
        return;
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", creator=" + creator +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", fileId"+fileId+", peerId"+peerId+
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Block block = (Block) o;
        return index == block.index
                && timestamp.equals(block.timestamp)
                && hash.equals(block.hash)
                && previousHash.equals(block.previousHash)
                && creator.equals(block.creator);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + hash.hashCode();
        result = 31 * result + previousHash.hashCode();
        result = 31 * result + creator.hashCode();
        return result;
    }

    public Block(String mode,int index,String hash, String preHash, String creator, String peerId, String fileId) {
        this.mode=mode;
        this.index = index;
        this.previousHash = preHash;
        this.creator = creator;
        this.fileId = fileId;
        this.peerId= peerId;
        timestamp = System.currentTimeMillis();
        this.hash = hash;
    }

    public String getFileId(){
        return fileId;
    }

    public String getPeer(){
        return peerId;
    }

    public String getCreator() {
        return creator;
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
    public String getMode() {
        return mode;
    }

    private String calculateHash(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "HASH_ERROR";
        }

        final byte bytes[] = digest.digest(text.getBytes());
        final StringBuilder hexString = new StringBuilder();
        for (final byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
