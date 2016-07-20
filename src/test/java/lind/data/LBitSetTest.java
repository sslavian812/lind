package lind.data;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * User: vitaly.khudobakhshov
 * Date: 19/07/16
 */
public class LBitSetTest {
    @Test
    public void testSet() throws Exception {
        LBitSet set = new LBitSet(1 << 20, 16);
        Random r = new Random(1);
        for(int i = 0; i < set.getNumBits(); i++) {
            boolean b = r.nextBoolean();
            if (b) set.set(i);
        }
        r = new Random(1);
        for(int i = 0; i < set.getNumBits(); i++) {
            assertEquals(r.nextBoolean(), set.get(i));
        }
    }

    @Test
    public void testUnset() throws Exception {
        LBitSet set = new LBitSet(1000000, 16);
        Random r = new Random(1);
        for(int i = 0; i < set.getNumBits(); i++) {
            set.set(i);
            if (r.nextBoolean()) set.unset(i);
        }
        r = new Random(1);
        for(int i = 0; i < set.getNumBits(); i++) {
            assertNotEquals(r.nextBoolean(), set.get(i));
        }
    }
}