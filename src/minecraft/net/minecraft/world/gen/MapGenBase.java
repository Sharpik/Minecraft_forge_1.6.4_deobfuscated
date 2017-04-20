package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class MapGenBase
{
    /** The number of Chunks to gen-check in any given direction. */
    protected int range = 8;

    /** The RNG used by the MapGen classes. */
    protected Random rand = new Random();

    /** This world object. */
    protected World worldObj;

    public void generate(IChunkProvider par1IChunkProvider, World par2World, int par3, int par4, byte[] par5ArrayOfByte)
    {
        int k = this.range;
        this.worldObj = par2World;
        this.rand.setSeed(par2World.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = par3 - k; j1 <= par3 + k; ++j1)
        {
            for (int k1 = par4 - k; k1 <= par4 + k; ++k1)
            {
                long l1 = (long)j1 * l;
                long i2 = (long)k1 * i1;
                this.rand.setSeed(l1 ^ i2 ^ par2World.getSeed());
                this.recursiveGenerate(par2World, j1, k1, par3, par4, par5ArrayOfByte);
            }
        }
    }

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, byte[] par6ArrayOfByte) {}
}
