package lind.array;

import lind.tuple.LInt2;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * User: vitaly.khudobakhshov
 * Date: 17/07/16
 */
public class LLongArrayTest {

    @Test
    public void testInit() throws Exception {
        LLongArray arr = new LLongArray((long) 1000000, 16);
        assertEquals(65536, arr.getChunkSize());
        assertEquals((long) 1000000, arr.getLength());
        assertEquals(1000000 / arr.getChunkSize() + 1, arr.getNumChunks());
        assertEquals(1000000 % arr.getChunkSize(), arr.getRestSize());
    }

    @Test
    public void testAddress() throws Exception {
        LLongArray arr = new LLongArray((long) 1000000, 16);
        LInt2 a = arr.address(400000);
        assertEquals(400000 / 65536, a._1);
        assertEquals(400000 % 65536, a._2);
    }

    @Test
    public void testGetSet() throws Exception {
        LLongArray arr = new LLongArray((long) 1000000, 16);
        for(long i = 0; i < arr.getLength(); i++) {
            arr.set(i, i + 10);
        }
        for(long i = 0; i < arr.getLength(); i++) {
            assertEquals(i + 10, arr.get(i));
        }
    }
}