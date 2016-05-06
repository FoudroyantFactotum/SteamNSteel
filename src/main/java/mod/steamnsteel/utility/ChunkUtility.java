package mod.steamnsteel.utility;

import net.minecraft.util.MathHelper;

public final class ChunkUtility
{
    //taken from MCs BlockPos class
    private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int X_SHIFT = NUM_Z_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    /**
     * Chunk Pos
     * @param x chunkPos
     * @param z chunkPos
     * @return chunkID
     */
    public static long toChunk(int x, int z)
    {
        return ((x & X_MASK) << X_SHIFT) | (z & Z_MASK);
    }

    /**
     *
     * @param chunk chunkID
     * @return chunk X
     */
    public static int toChunkX(long chunk)
    {
        return (int) (((chunk & (X_MASK << NUM_X_BITS)) << (Long.BYTES*8 - X_SHIFT - NUM_X_BITS) >> Long.BYTES*8 - NUM_X_BITS));
    }

    /**
     *
     * @param chunk chunkID
     * @return chunk Z
     */
    public static int toChunkZ(long chunk)
    {
        return (int) ((chunk & Z_MASK) << (Long.BYTES*8 - NUM_Z_BITS) >> Long.BYTES*8 - NUM_Z_BITS);
    }
}
