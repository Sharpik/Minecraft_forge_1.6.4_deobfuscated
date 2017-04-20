package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class WorldGenHugeTrees extends WorldGenerator
{
    /** The base height of the tree */
    private final int baseHeight;

    /** Sets the metadata for the wood blocks used */
    private final int woodMetadata;

    /** Sets the metadata for the leaves used in huge trees */
    private final int leavesMetadata;

    public WorldGenHugeTrees(boolean par1, int par2, int par3, int par4)
    {
        super(par1);
        this.baseHeight = par2;
        this.woodMetadata = par3;
        this.leavesMetadata = par4;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        int l = par2Random.nextInt(3) + this.baseHeight;
        boolean flag = true;

        if (par4 >= 1 && par4 + l + 1 <= 256)
        {
            int i1;
            int j1;
            int k1;
            int l1;

            for (i1 = par4; i1 <= par4 + 1 + l; ++i1)
            {
                byte b0 = 2;

                if (i1 == par4)
                {
                    b0 = 1;
                }

                if (i1 >= par4 + 1 + l - 2)
                {
                    b0 = 2;
                }

                for (j1 = par3 - b0; j1 <= par3 + b0 && flag; ++j1)
                {
                    for (k1 = par5 - b0; k1 <= par5 + b0 && flag; ++k1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            l1 = par1World.getBlockId(j1, i1, k1);
                            Block block = Block.blocksList[l1];

                            if (block != null &&
                               !block.isAirBlock(par1World, j1, i1, k1) &&
                               !block.isLeaves(par1World, j1, i1, k1) &&
                               !block.isWood(par1World, j1, i1, k1) &&
                                block != Block.grass &&
                                block != Block.dirt &&
                                block != Block.sapling)
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
                i1 = par1World.getBlockId(par3, par4 - 1, par5);

                Block soil = Block.blocksList[i1];
                boolean isValidSoil = soil != null && soil.canSustainPlant(par1World, par3, par4 - 1, par5, ForgeDirection.UP, (BlockSapling)Block.sapling);

                if (isValidSoil && par4 < 256 - l - 1)
                {
                    onPlantGrow(par1World, par3,     par4 - 1, par5,     par3, par4, par5);
                    onPlantGrow(par1World, par3 + 1, par4 - 1, par5,     par3, par4, par5);
                    onPlantGrow(par1World, par3,     par4 - 1, par5 + 1, par3, par4, par5);
                    onPlantGrow(par1World, par3 + 1, par4 - 1, par5 + 1, par3, par4, par5);
                    this.growLeaves(par1World, par3, par5, par4 + l, 2, par2Random);

                    for (int i2 = par4 + l - 2 - par2Random.nextInt(4); i2 > par4 + l / 2; i2 -= 2 + par2Random.nextInt(4))
                    {
                        float f = par2Random.nextFloat() * (float)Math.PI * 2.0F;
                        k1 = par3 + (int)(0.5F + MathHelper.cos(f) * 4.0F);
                        l1 = par5 + (int)(0.5F + MathHelper.sin(f) * 4.0F);
                        this.growLeaves(par1World, k1, l1, i2, 0, par2Random);

                        for (int j2 = 0; j2 < 5; ++j2)
                        {
                            k1 = par3 + (int)(1.5F + MathHelper.cos(f) * (float)j2);
                            l1 = par5 + (int)(1.5F + MathHelper.sin(f) * (float)j2);
                            this.setBlockAndMetadata(par1World, k1, i2 - 3 + j2 / 2, l1, Block.wood.blockID, this.woodMetadata);
                        }
                    }

                    for (j1 = 0; j1 < l; ++j1)
                    {
                        k1 = par1World.getBlockId(par3, par4 + j1, par5);

                        if (isReplaceable(par1World, par3, par4 + j1, par5))
                        {
                            this.setBlockAndMetadata(par1World, par3, par4 + j1, par5, Block.wood.blockID, this.woodMetadata);

                            if (j1 > 0)
                            {
                                if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + j1, par5))
                                {
                                    this.setBlockAndMetadata(par1World, par3 - 1, par4 + j1, par5, Block.vine.blockID, 8);
                                }

                                if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 - 1))
                                {
                                    this.setBlockAndMetadata(par1World, par3, par4 + j1, par5 - 1, Block.vine.blockID, 1);
                                }
                            }
                        }

                        if (j1 < l - 1)
                        {
                            k1 = par1World.getBlockId(par3 + 1, par4 + j1, par5);

                            if (isReplaceable(par1World, par3 + 1, par4 + j1, par5))
                            {
                                this.setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5, Block.wood.blockID, this.woodMetadata);

                                if (j1 > 0)
                                {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 2, par4 + j1, par5))
                                    {
                                        this.setBlockAndMetadata(par1World, par3 + 2, par4 + j1, par5, Block.vine.blockID, 2);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + j1, par5 - 1))
                                    {
                                        this.setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5 - 1, Block.vine.blockID, 1);
                                    }
                                }
                            }

                            k1 = par1World.getBlockId(par3 + 1, par4 + j1, par5 + 1);

                            if (isReplaceable(par1World, par3 + 1, par4 + j1, par5 + 1))
                            {
                                this.setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5 + 1, Block.wood.blockID, this.woodMetadata);

                                if (j1 > 0)
                                {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 2, par4 + j1, par5 + 1))
                                    {
                                        this.setBlockAndMetadata(par1World, par3 + 2, par4 + j1, par5 + 1, Block.vine.blockID, 2);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + j1, par5 + 2))
                                    {
                                        this.setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5 + 2, Block.vine.blockID, 4);
                                    }
                                }
                            }

                            k1 = par1World.getBlockId(par3, par4 + j1, par5 + 1);

                            if (isReplaceable(par1World, par3, par4 + j1, par5 + 1))
                            {
                                this.setBlockAndMetadata(par1World, par3, par4 + j1, par5 + 1, Block.wood.blockID, this.woodMetadata);

                                if (j1 > 0)
                                {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + j1, par5 + 1))
                                    {
                                        this.setBlockAndMetadata(par1World, par3 - 1, par4 + j1, par5 + 1, Block.vine.blockID, 8);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 + 2))
                                    {
                                        this.setBlockAndMetadata(par1World, par3, par4 + j1, par5 + 2, Block.vine.blockID, 4);
                                    }
                                }
                            }
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

    private void growLeaves(World par1World, int par2, int par3, int par4, int par5, Random par6Random)
    {
        byte b0 = 2;

        for (int i1 = par4 - b0; i1 <= par4; ++i1)
        {
            int j1 = i1 - par4;
            int k1 = par5 + 1 - j1;

            for (int l1 = par2 - k1; l1 <= par2 + k1 + 1; ++l1)
            {
                int i2 = l1 - par2;

                for (int j2 = par3 - k1; j2 <= par3 + k1 + 1; ++j2)
                {
                    int k2 = j2 - par3;

                    if ((i2 >= 0 || k2 >= 0 || i2 * i2 + k2 * k2 <= k1 * k1) && (i2 <= 0 && k2 <= 0 || i2 * i2 + k2 * k2 <= (k1 + 1) * (k1 + 1)) && (par6Random.nextInt(4) != 0 || i2 * i2 + k2 * k2 <= (k1 - 1) * (k1 - 1)))
                    {
                        int l2 = par1World.getBlockId(l1, i1, j2);
                        Block block = Block.blocksList[l2];

                        if (block == null || block.canBeReplacedByLeaves(par1World, l1, i1, j2))
                        {
                            this.setBlockAndMetadata(par1World, l1, i1, j2, Block.leaves.blockID, this.leavesMetadata);
                        }
                    }
                }
            }
        }
    }

    private void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != null)
        {
            block.onPlantGrow(world, x, y, z, sourceX, sourceY, sourceZ);
        }
    }

    private boolean isReplaceable(World world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        return (block == null || block.isAirBlock(world, x, y, z) || block.isLeaves(world, x, y, z));
    }
}
