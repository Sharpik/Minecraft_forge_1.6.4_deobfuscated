package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator
{
    /** Stores the ID for WorldGenHellLava */
    private int hellLavaID;
    private boolean field_94524_b;

    public WorldGenHellLava(int par1, boolean par2)
    {
        this.hellLavaID = par1;
        this.field_94524_b = par2;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        if (par1World.getBlockId(par3, par4 + 1, par5) != Block.netherrack.blockID)
        {
            return false;
        }
        else if (par1World.getBlockId(par3, par4, par5) != 0 && par1World.getBlockId(par3, par4, par5) != Block.netherrack.blockID)
        {
            return false;
        }
        else
        {
            int l = 0;

            if (par1World.getBlockId(par3 - 1, par4, par5) == Block.netherrack.blockID)
            {
                ++l;
            }

            if (par1World.getBlockId(par3 + 1, par4, par5) == Block.netherrack.blockID)
            {
                ++l;
            }

            if (par1World.getBlockId(par3, par4, par5 - 1) == Block.netherrack.blockID)
            {
                ++l;
            }

            if (par1World.getBlockId(par3, par4, par5 + 1) == Block.netherrack.blockID)
            {
                ++l;
            }

            if (par1World.getBlockId(par3, par4 - 1, par5) == Block.netherrack.blockID)
            {
                ++l;
            }

            int i1 = 0;

            if (par1World.isAirBlock(par3 - 1, par4, par5))
            {
                ++i1;
            }

            if (par1World.isAirBlock(par3 + 1, par4, par5))
            {
                ++i1;
            }

            if (par1World.isAirBlock(par3, par4, par5 - 1))
            {
                ++i1;
            }

            if (par1World.isAirBlock(par3, par4, par5 + 1))
            {
                ++i1;
            }

            if (par1World.isAirBlock(par3, par4 - 1, par5))
            {
                ++i1;
            }

            if (!this.field_94524_b && l == 4 && i1 == 1 || l == 5)
            {
                par1World.setBlock(par3, par4, par5, this.hellLavaID, 0, 2);
                par1World.scheduledUpdatesAreImmediate = true;
                Block.blocksList[this.hellLavaID].updateTick(par1World, par3, par4, par5, par2Random);
                par1World.scheduledUpdatesAreImmediate = false;
            }

            return true;
        }
    }
}
