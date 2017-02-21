package lind.array;

import lind.tuple.LInt2;

import java.io.Serializable;

/**
 * User: vitaly.khudobakhshov
 * Date: 17/07/16
 */
public abstract class LArray implements Serializable {
    protected long length;
    protected int chunkSize;
    protected int chunkSizeLog2;
    protected int restSize;
    protected int numChunks;

    protected LArray(long length, int chunkSizeLog2) {
        this.length = length;
        this.chunkSizeLog2 = chunkSizeLog2;
        this.chunkSize = 1 << chunkSizeLog2;
        this.restSize = (int) (length & (chunkSize - 1)); // length % chunkSize
        this.numChunks = (int) (length / chunkSize) + (restSize > 0 ? 1 : 0);
    }

    LInt2 address(long index) {
        // (index / chunkSize, index % chunkSize)
        return new LInt2((int) (index >> chunkSizeLog2), (int) (index & (chunkSize - 1)));
    }

    public long getLength() {
        return length;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getChunkSizeLog2() {
        return chunkSizeLog2;
    }

    public int getRestSize() {
        return restSize;
    }

    public int getNumChunks() {
        return numChunks;
    }
}
