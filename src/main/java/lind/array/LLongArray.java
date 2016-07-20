package lind.array;

import lind.tuple.LInt2;

import java.io.Serializable;

/**
 * User: vitaly.khudobakhshov
 * Date: 17/07/16
 */
public class LLongArray extends LArray implements Serializable {
    private long[][] chunks;
    private static final long serialVersionUID = 1L;

    public LLongArray(long length, int chunkSizeLog2) {
        super(length, chunkSizeLog2);

        chunks = new long[numChunks][];
        for (int i = 0; i < numChunks; i++)
            chunks[i] = new long[chunkSize];
    }

    public long get(long index) {
        LInt2 a = address(index);
        return chunks[a._1][a._2];
    }

    public void set(long index, long value) {
        LInt2 a = address(index);
        chunks[a._1][a._2] = value;
    }
}
