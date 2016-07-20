package lind.data;

/**
 * User: vitaly.khudobakhshov
 * Date: 19/07/16
 */
public class LGenericBloomFilter<T extends LPackable> extends LBloomFilter {

    public LGenericBloomFilter(int numHashes, long numBits,
                        int chunkSizeLog2, LHashingStrategy hashingStrategy) {
        super(numHashes, numBits, chunkSizeLog2, hashingStrategy);
    }

    public void add(T obj) {
        addBytes(obj.pack());
    }

    public boolean contains(T obj) {
        return containsBytes(obj.pack());
    }
}
