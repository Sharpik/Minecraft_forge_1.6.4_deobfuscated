package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;

public class StructureMineshaftPieces
{
    /** List of contents that can generate in Mineshafts. */
    public static final WeightedRandomChestContent[] mineshaftChestContents = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5), new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5), new WeightedRandomChestContent(Item.dyePowder.itemID, 4, 4, 9, 5), new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 2, 3), new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10), new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1), new WeightedRandomChestContent(Block.rail.blockID, 0, 4, 8, 1), new WeightedRandomChestContent(Item.melonSeeds.itemID, 0, 2, 4, 10), new WeightedRandomChestContent(Item.pumpkinSeeds.itemID, 0, 2, 4, 10), new WeightedRandomChestContent(Item.saddle.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.horseArmorIron.itemID, 0, 1, 1, 1)};

    public static void func_143048_a()
    {
        MapGenStructureIO.func_143031_a(ComponentMineshaftCorridor.class, "MSCorridor");
        MapGenStructureIO.func_143031_a(ComponentMineshaftCross.class, "MSCrossing");
        MapGenStructureIO.func_143031_a(ComponentMineshaftRoom.class, "MSRoom");
        MapGenStructureIO.func_143031_a(ComponentMineshaftStairs.class, "MSStairs");
    }

    private static StructureComponent getRandomComponent(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        int j1 = par1Random.nextInt(100);
        StructureBoundingBox structureboundingbox;

        if (j1 >= 80)
        {
            structureboundingbox = ComponentMineshaftCross.findValidPlacement(par0List, par1Random, par2, par3, par4, par5);

            if (structureboundingbox != null)
            {
                return new ComponentMineshaftCross(par6, par1Random, structureboundingbox, par5);
            }
        }
        else if (j1 >= 70)
        {
            structureboundingbox = ComponentMineshaftStairs.findValidPlacement(par0List, par1Random, par2, par3, par4, par5);

            if (structureboundingbox != null)
            {
                return new ComponentMineshaftStairs(par6, par1Random, structureboundingbox, par5);
            }
        }
        else
        {
            structureboundingbox = ComponentMineshaftCorridor.findValidPlacement(par0List, par1Random, par2, par3, par4, par5);

            if (structureboundingbox != null)
            {
                return new ComponentMineshaftCorridor(par6, par1Random, structureboundingbox, par5);
            }
        }

        return null;
    }

    private static StructureComponent getNextMineShaftComponent(StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 8)
        {
            return null;
        }
        else if (Math.abs(par3 - par0StructureComponent.getBoundingBox().minX) <= 80 && Math.abs(par5 - par0StructureComponent.getBoundingBox().minZ) <= 80)
        {
            StructureComponent structurecomponent1 = getRandomComponent(par1List, par2Random, par3, par4, par5, par6, par7 + 1);

            if (structurecomponent1 != null)
            {
                par1List.add(structurecomponent1);
                structurecomponent1.buildComponent(par0StructureComponent, par1List, par2Random);
            }

            return structurecomponent1;
        }
        else
        {
            return null;
        }
    }

    static StructureComponent getNextComponent(StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextMineShaftComponent(par0StructureComponent, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static WeightedRandomChestContent[] func_78816_a()
    {
        return mineshaftChestContents;
    }
}
