package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class WorldGeneratorBonusChest extends WorldGenerator
{
    /**
     * Instance of WeightedRandomChestContent what will randomly generate items into the Bonus Chest.
     */
    private final WeightedRandomChestContent[] theBonusChestGenerator;

    /**
     * Value of this int will determine how much items gonna generate in Bonus Chest.
     */
    private final int itemsToGenerateInBonusChest;

    public WorldGeneratorBonusChest(WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, int par2)
    {
        this.theBonusChestGenerator = par1ArrayOfWeightedRandomChestContent;
        this.itemsToGenerateInBonusChest = par2;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        int l;

        for (boolean flag = false; ((l = par1World.getBlockId(par3, par4, par5)) == 0 || l == Block.leaves.blockID) && par4 > 1; --par4)
        {
            ;
        }

        if (par4 < 1)
        {
            return false;
        }
        else
        {
            ++par4;

            for (int i1 = 0; i1 < 4; ++i1)
            {
                int j1 = par3 + par2Random.nextInt(4) - par2Random.nextInt(4);
                int k1 = par4 + par2Random.nextInt(3) - par2Random.nextInt(3);
                int l1 = par5 + par2Random.nextInt(4) - par2Random.nextInt(4);

                if (par1World.isAirBlock(j1, k1, l1) && par1World.doesBlockHaveSolidTopSurface(j1, k1 - 1, l1))
                {
                    par1World.setBlock(j1, k1, l1, Block.chest.blockID, 0, 2);
                    TileEntityChest tileentitychest = (TileEntityChest)par1World.getBlockTileEntity(j1, k1, l1);

                    if (tileentitychest != null && tileentitychest != null)
                    {
                        WeightedRandomChestContent.generateChestContents(par2Random, this.theBonusChestGenerator, tileentitychest, this.itemsToGenerateInBonusChest);
                    }

                    if (par1World.isAirBlock(j1 - 1, k1, l1) && par1World.doesBlockHaveSolidTopSurface(j1 - 1, k1 - 1, l1))
                    {
                        par1World.setBlock(j1 - 1, k1, l1, Block.torchWood.blockID, 0, 2);
                    }

                    if (par1World.isAirBlock(j1 + 1, k1, l1) && par1World.doesBlockHaveSolidTopSurface(j1 - 1, k1 - 1, l1))
                    {
                        par1World.setBlock(j1 + 1, k1, l1, Block.torchWood.blockID, 0, 2);
                    }

                    if (par1World.isAirBlock(j1, k1, l1 - 1) && par1World.doesBlockHaveSolidTopSurface(j1 - 1, k1 - 1, l1))
                    {
                        par1World.setBlock(j1, k1, l1 - 1, Block.torchWood.blockID, 0, 2);
                    }

                    if (par1World.isAirBlock(j1, k1, l1 + 1) && par1World.doesBlockHaveSolidTopSurface(j1 - 1, k1 - 1, l1))
                    {
                        par1World.setBlock(j1, k1, l1 + 1, Block.torchWood.blockID, 0, 2);
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
