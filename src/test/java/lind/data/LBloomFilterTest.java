package lind.data;

import com.google.common.hash.Hashing;
import lind.tuple.LIntLong;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * User: vitaly.khudobakhshov
 * Date: 19/07/16
 */
public class LBloomFilterTest {

    @Test
    public void testOptimalParameters() throws Exception {
        // from http://hur.st/bloomfilter?n=4&p=1.0E-20
        LIntLong p = LBloomFilter.optimalParameters(0.01, 4);
        assertEquals(7, p._1);
        assertEquals(39, p._2);
    }

    @Test
    public void testInAction() throws Exception {
        LIntLong p = LBloomFilter.optimalParameters(0.01, 100);

        LBloomFilter bf = new LBloomFilter(p._1, p._2, 8, mumur3Strategy);
        for(int i = 0; i < 100; i ++)
            bf.addBytes(String.valueOf(i).getBytes());

        int fp = 0;
        for(int i = 101; i < 1000; i++)
            if (bf.containsBytes(String.valueOf(i).getBytes())) fp++;

        assertTrue(fp / 1000.0 < 0.01);
    }

    @Test
    public void testAddContains() throws Exception {
        LBloomFilter bf = new LBloomFilter(6, 1000000, 16, mumur3Strategy);
        bf.addBytes("hello".getBytes());
        assertTrue(bf.containsBytes("hello".getBytes()));
        assertFalse(bf.containsBytes("test".getBytes()));
    }

    private static LBloomFilter.LHashingStrategy mumur3Strategy = new LBloomFilter.LHashingStrategy() {
        int salt = 0xAF;

        public long[] hash(byte[] data, int numHashes) {
            long[] h = new long[numHashes];
            for(int i = 0; i < numHashes; i++)
                h[i] = Hashing.murmur3_128(i + salt).hashBytes(data).asLong();
            return h;
        }
    };
}