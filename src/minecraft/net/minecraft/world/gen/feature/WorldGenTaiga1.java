package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenerator
{
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        int l = par2Random.nextInt(5) + 7;
        int i1 = l - par2Random.nextInt(2) - 3;
        int j1 = l - i1;
        int k1 = 1 + par2Random.nextInt(j1 + 1);
        boolean flag = true;

        if (par4 >= 1 && par4 + l + 1 <= 128)
        {
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;

            for (l1 = par4; l1 <= par4 + 1 + l && flag; ++l1)
            {
                boolean flag1 = true;

                if (l1 - par4 < i1)
                {
                    l2 = 0;
                }
                else
                {
                    l2 = k1;
                }

                for (i2 = par3 - l2; i2 <= par3 + l2 && flag; ++i2)
                {
                    for (j2 = par5 - l2; j2 <= par5 + l2 && flag; ++j2)
                    {
                        if (l1 >= 0 && l1 < 128)
                        {
                            k2 = par1World.getBlockId(i2, l1, j2);

                            Block block = Block.blocksList[k2];

                            if (k2 != 0 && (block == null || !block.isLeaves(par1World, i2, l1, j2)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                l1 = par1World.getBlockId(par3, par4 - 1, par5);

                if ((l1 == Block.grass.blockID || l1 == Block.dirt.blockID) && par4 < 128 - l - 1)
                {
                    this.setBlock(par1World, par3, par4 - 1, par5, Block.dirt.blockID);
                    l2 = 0;

                    for (i2 = par4 + l; i2 >= par4 + i1; --i2)
                    {
                        for (j2 = par3 - l2; j2 <= par3 + l2; ++j2)
                        {
                            k2 = j2 - par3;

                            for (int i3 = par5 - l2; i3 <= par5 + l2; ++i3)
                            {
                                int j3 = i3 - par5;

                                Block block = Block.blocksList[par1World.getBlockId(j2, i2, i3)];

                                if ((Math.abs(k2) != l2 || Math.abs(j3) != l2 || l2 <= 0) && 
                                    (block == null || block.canBeReplacedByLeaves(par1World, j2, i2, i3)))
                                {
                                    this.setBlockAndMetadata(par1World, j2, i2, i3, Block.leaves.blockID, 1);
                                }
                            }
                        }

                        if (l2 >= 1 && i2 == par4 + i1 + 1)
                        {
                            --l2;
                        }
                        else if (l2 < k1)
                        {
                            ++l2;
                        }
                    }

                    for (i2 = 0; i2 < l - 1; ++i2)
                    {
                        j2 = par1World.getBlockId(par3, par4 + i2, par5);

                        Block block = Block.blocksList[j2];

                        if (j2 == 0 || block == null || block.isLeaves(par1World, par3, par4 + i2, par5))
                        {
                            this.setBlockAndMetadata(par1World, par3, par4 + i2, par5, Block.wood.blockID, 1);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
