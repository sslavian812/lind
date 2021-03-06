package lind.data;

import com.google.common.hash.Hashing;
import lind.tuple.LIntLong;

import java.io.Serializable;

import static java.lang.Math.*;

/**
 * User: vitaly.khudobakhshov
 * Date: 19/07/16
 */
public class LBloomFilter implements Serializable {

    public interface LHashingStrategy extends Serializable {
        long[] hash(byte[] data, int numHashes);
    }

    private final int numHashes;
    private final long numBits;
    private final LBitSet bits;
    private final LHashingStrategy hashingStrategy;
    private static final long mask = 0x7FFFFFFFFFFFFFFFL;

    public LBloomFilter(int numHashes, long numBits,
                        int chunkSizeLog2, LHashingStrategy hashingStrategy) {
        this.numHashes = numHashes;
        this.numBits = numBits;
        this.bits = new LBitSet(numBits, chunkSizeLog2);
        this.hashingStrategy = hashingStrategy;
    }

    public LBloomFilter(double fpProb, long maxNumEntries, int chunkSizeLog2) {
        LIntLong parameters = optimalParameters(fpProb, maxNumEntries);
        this.numHashes = parameters._1;
        this.numBits = parameters._2;
        this.bits = new LBitSet(numBits, chunkSizeLog2);
        this.hashingStrategy = mumur3Strategy;
    }

    /**
     * Used as default hashing strategy.
     */
    private static LBloomFilter.LHashingStrategy mumur3Strategy = new LBloomFilter.LHashingStrategy() {
        int salt = 0xAF;

        public long[] hash(byte[] data, int numHashes) {
            long[] h = new long[numHashes];
            for (int i = 0; i < numHashes; i++)
                h[i] = Hashing.murmur3_128(i + salt).hashBytes(data).asLong();
            return h;
        }
    };

    public boolean containsBytes(byte[] bytes) {
        long[] hashes = hashingStrategy.hash(bytes, numHashes);

        for(long h: hashes)
          if (!bits.get((h & mask) % numBits)) return false;

        return true;
    }

    public void addBytes(byte[] bytes) {
        long[] hashes = hashingStrategy.hash(bytes, numHashes);

        for(long h: hashes)
          bits.set((h & mask) % numBits);
    }

    public static LIntLong optimalParameters(double fpProb, long maxNumEntries) {
        long numBits = (long)ceil(maxNumEntries * log(fpProb) / log(1.0 / pow(2.0, log(2.0))));
        int numHashes = (int)round(log(2.0) * numBits / maxNumEntries);
        return new LIntLong(numHashes, numBits);
    }
}
