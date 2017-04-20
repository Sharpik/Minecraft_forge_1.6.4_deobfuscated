package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator
{
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        while (par1World.isAirBlock(par3, par4, par5) && par4 > 2)
        {
            --par4;
        }

        int l = par1World.getBlockId(par3, par4, par5);

        if (l != Block.sand.blockID)
        {
            return false;
        }
        else
        {
            int i1;
            int j1;

            for (i1 = -2; i1 <= 2; ++i1)
            {
                for (j1 = -2; j1 <= 2; ++j1)
                {
                    if (par1World.isAirBlock(par3 + i1, par4 - 1, par5 + j1) && par1World.isAirBlock(par3 + i1, par4 - 2, par5 + j1))
                    {
                        return false;
                    }
                }
            }

            for (i1 = -1; i1 <= 0; ++i1)
            {
                for (j1 = -2; j1 <= 2; ++j1)
                {
                    for (int k1 = -2; k1 <= 2; ++k1)
                    {
                        par1World.setBlock(par3 + j1, par4 + i1, par5 + k1, Block.sandStone.blockID, 0, 2);
                    }
                }
            }

            par1World.setBlock(par3, par4, par5, Block.waterMoving.blockID, 0, 2);
            par1World.setBlock(par3 - 1, par4, par5, Block.waterMoving.blockID, 0, 2);
            par1World.setBlock(par3 + 1, par4, par5, Block.waterMoving.blockID, 0, 2);
            par1World.setBlock(par3, par4, par5 - 1, Block.waterMoving.blockID, 0, 2);
            par1World.setBlock(par3, par4, par5 + 1, Block.waterMoving.blockID, 0, 2);

            for (i1 = -2; i1 <= 2; ++i1)
            {
                for (j1 = -2; j1 <= 2; ++j1)
                {
                    if (i1 == -2 || i1 == 2 || j1 == -2 || j1 == 2)
                    {
                        par1World.setBlock(par3 + i1, par4 + 1, par5 + j1, Block.sandStone.blockID, 0, 2);
                    }
                }
            }

            par1World.setBlock(par3 + 2, par4 + 1, par5, Block.stoneSingleSlab.blockID, 1, 2);
            par1World.setBlock(par3 - 2, par4 + 1, par5, Block.stoneSingleSlab.blockID, 1, 2);
            par1World.setBlock(par3, par4 + 1, par5 + 2, Block.stoneSingleSlab.blockID, 1, 2);
            par1World.setBlock(par3, par4 + 1, par5 - 2, Block.stoneSingleSlab.blockID, 1, 2);

            for (i1 = -1; i1 <= 1; ++i1)
            {
                for (j1 = -1; j1 <= 1; ++j1)
                {
                    if (i1 == 0 && j1 == 0)
                    {
                        par1World.setBlock(par3 + i1, par4 + 4, par5 + j1, Block.sandStone.blockID, 0, 2);
                    }
                    else
                    {
                        par1World.setBlock(par3 + i1, par4 + 4, par5 + j1, Block.stoneSingleSlab.blockID, 1, 2);
                    }
                }
            }

            for (i1 = 1; i1 <= 3; ++i1)
            {
                par1World.setBlock(par3 - 1, par4 + i1, par5 - 1, Block.sandStone.blockID, 0, 2);
                par1World.setBlock(par3 - 1, par4 + i1, par5 + 1, Block.sandStone.blockID, 0, 2);
                par1World.setBlock(par3 + 1, par4 + i1, par5 - 1, Block.sandStone.blockID, 0, 2);
                par1World.setBlock(par3 + 1, par4 + i1, par5 + 1, Block.sandStone.blockID, 0, 2);
            }

            return true;
        }
    }
}
