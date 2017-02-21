package lind.data;

import lind.array.LLongArray;
import org.junit.Test;

import java.io.*;
import java.util.Random;

import static lind.data.LBloomFilterTest.mumur3Strategy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by vyacheslav.shalamov on 16.02.17.
 */
public class SerializationTest {
    @Test
    public void testSerializeLLongArray() throws Exception {
        LLongArray array = new LLongArray(1000,5);
        array.set(0, 1L);
        array.set(999, 2L);

        File file = File.createTempFile(LLongArray.class.getName(), null);
        file.deleteOnExit();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));
        out.writeObject(array);
        array = null;
        System.gc();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        array = (LLongArray) in.readObject();

        assertEquals(1000, array.getLength());
        assertEquals(1L, array.get(0));
        assertEquals(2L, array.get(999));
        assertEquals(0, array.get(2));
    }


    @Test
    public void testSerializeLBitSet() throws Exception {
        LBitSet set = new LBitSet(1 << 20, 16);
        Random r = new Random(1);
        for(int i = 0; i < set.getNumBits(); i++) {
            boolean b = r.nextBoolean();
            if (b) set.set(i);
        }

        File file = File.createTempFile(LBitSet.class.getName(), null);
        file.deleteOnExit();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));
        out.writeObject(set);
        set = null;
        System.gc();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        set = (LBitSet) in.readObject();

        r = new Random(1);
        for(int i = 0; i < set.getNumBits(); i++) {
            assertEquals(r.nextBoolean(), set.get(i));
        }
    }

    @Test
    public void testSerializeLBloomFilter() throws Exception {
        LBloomFilter bf = new LBloomFilter(6, 1000000, 16, mumur3Strategy);
        bf.addBytes("hello".getBytes());

        File file = File.createTempFile(LBloomFilter.class.getName(), null);
        file.deleteOnExit();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));
        out.writeObject(bf);
        bf = null;
        System.gc();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        bf = (LBloomFilter) in.readObject();

        assertTrue(bf.containsBytes("hello".getBytes()));
        assertFalse(bf.containsBytes("test".getBytes()));
    }

    @Test
    public void testSerializeToBytesLBloomFilter() throws Exception {
        LBloomFilter bf = new LBloomFilter(6, 1000000, 16, mumur3Strategy);
        bf.addBytes("hello".getBytes());

        File file = File.createTempFile(LBloomFilter.class.getName(), null);
        file.deleteOnExit();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput output = null;
        try {
            output = new ObjectOutputStream(bos);
            output.writeObject(bf);
            output.flush();
            byte[] bytes = bos.toByteArray();

            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        bf = null;
        System.gc();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        bf = (LBloomFilter) in.readObject();

        assertTrue(bf.containsBytes("hello".getBytes()));
        assertFalse(bf.containsBytes("test".getBytes()));
    }

    /**
     * For a fixed false positive probability 0.02, for each billion of entries, a Gigabyte of data will be stored.
     * This test will create and delete a file of size ~ 6 Gb.
     * Note: you should increase you heap by adding -Xmx8G to VM options.
     * @throws Exception
     */
    @Test
    public void testExtremelyLargeBloomFilterSerialization() throws Exception {

        LBloomFilter bf = new LBloomFilter(0.02, 6000000000L, 16);
        bf.addBytes("hello".getBytes());

        File file = File.createTempFile(LBloomFilter.class.getName(), null);
        file.deleteOnExit();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false));
        out.writeObject(bf);
        out.close();
        bf = null;
        System.gc();
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        bf = (LBloomFilter) in.readObject();
        in.close();

        assertTrue(bf.containsBytes("hello".getBytes()));
        assertFalse(bf.containsBytes("test".getBytes()));
    }
}
