package lind.data;

import lind.array.LLongArray;

/**
 * User: vitaly.khudobakhshov
 * Date: 17/07/16
 */
public class LBitSet {
    private final LLongArray words;
    private long numBits;

    public LBitSet(long numBits, int chunkSizeLog2) {
        this.words = new LLongArray(wordsRequired(numBits), chunkSizeLog2);
        this.numBits = numBits;
    }

    public void set(long index) {
        long bitmask = 1L << (index & 63); // mod 64 and shift
        long wordIndex = index >> 6;
        long w = words.get(wordIndex);
        words.set(wordIndex, w | bitmask); // div by 64 and mask
    }

    public void unset(long index) {
        long bitmask = 1L << (index & 63);  // mod 64 and shift
        long wordIndex = index >> 6;
        long w = words.get(wordIndex);
        words.set(wordIndex, w & ~bitmask); // div by 64 and mask
    }

    /**
     * Return the value of the bit with the specified index. The value is true if the bit with
     * the index is currently set in this BitSet; otherwise, the result is false.
     *
     * @param index the bit index
     * @return the value of the bit with the specified index
     */
    public boolean get(long index) {
        long bitmask = 1L << (index & 63);            // mod 64 and shift
        return (words.get(index >> 6) & bitmask) != 0;  // div by 64 and mask
    }

    public long getNumBits() {
        return numBits;
    }

    static long wordsRequired(long numBits) {
        return numBits >> 6;
    }
}
