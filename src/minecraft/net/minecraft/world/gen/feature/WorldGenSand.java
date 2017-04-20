package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class WorldGenSand extends WorldGenerator
{
    /** Stores ID for WorldGenSand */
    private int sandID;

    /** The maximum radius used when generating a patch of blocks. */
    private int radius;

    public WorldGenSand(int par1, int par2)
    {
        this.sandID = par2;
        this.radius = par1;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        if (par1World.getBlockMaterial(par3, par4, par5) != Material.water)
        {
            return false;
        }
        else
        {
            int l = par2Random.nextInt(this.radius - 2) + 2;
            byte b0 = 2;

            for (int i1 = par3 - l; i1 <= par3 + l; ++i1)
            {
                for (int j1 = par5 - l; j1 <= par5 + l; ++j1)
                {
                    int k1 = i1 - par3;
                    int l1 = j1 - par5;

                    if (k1 * k1 + l1 * l1 <= l * l)
                    {
                        for (int i2 = par4 - b0; i2 <= par4 + b0; ++i2)
                        {
                            int j2 = par1World.getBlockId(i1, i2, j1);

                            if (j2 == Block.dirt.blockID || j2 == Block.grass.blockID)
                            {
                                par1World.setBlock(i1, i2, j1, this.sandID, 0, 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
