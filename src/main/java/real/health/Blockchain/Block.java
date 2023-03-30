package real.health.Blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class Block {
    private final String data;
    private final long timestamp;
    private final String previousHash;
    private final String hash;

    public Block(String string, String previousHash) {
        this.data = string;
        this.previousHash = previousHash;
        this.timestamp = Instant.now().getEpochSecond();
        this.hash = generateHash();
    }

    String generateHash() {
        String input = data + timestamp + previousHash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Unable to generate block hash", e);
        }
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }
}

