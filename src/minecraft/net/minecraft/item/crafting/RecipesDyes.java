package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesDyes
{
    /**
     * Adds the dye recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager par1CraftingManager)
    {
        int i;

        for (i = 0; i < 16; ++i)
        {
            par1CraftingManager.addShapelessRecipe(new ItemStack(Block.cloth, 1, BlockColored.getDyeFromBlock(i)), new Object[] {new ItemStack(Item.dyePowder, 1, i), new ItemStack(Item.itemsList[Block.cloth.blockID], 1, 0)});
            par1CraftingManager.addRecipe(new ItemStack(Block.stainedClay, 8, BlockColored.getDyeFromBlock(i)), new Object[] {"###", "#X#", "###", '#', new ItemStack(Block.hardenedClay), 'X', new ItemStack(Item.dyePowder, 1, i)});
        }

        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 11), new Object[] {Block.plantYellow});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 1), new Object[] {Block.plantRed});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 15), new Object[] {Item.bone});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 9), new Object[] {new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 15)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 14), new Object[] {new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 11)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 10), new Object[] {new ItemStack(Item.dyePowder, 1, 2), new ItemStack(Item.dyePowder, 1, 15)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 8), new Object[] {new ItemStack(Item.dyePowder, 1, 0), new ItemStack(Item.dyePowder, 1, 15)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 7), new Object[] {new ItemStack(Item.dyePowder, 1, 8), new ItemStack(Item.dyePowder, 1, 15)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 7), new Object[] {new ItemStack(Item.dyePowder, 1, 0), new ItemStack(Item.dyePowder, 1, 15), new ItemStack(Item.dyePowder, 1, 15)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 12), new Object[] {new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 15)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 6), new Object[] {new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 2)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 5), new Object[] {new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 1)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 2, 13), new Object[] {new ItemStack(Item.dyePowder, 1, 5), new ItemStack(Item.dyePowder, 1, 9)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 3, 13), new Object[] {new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 9)});
        par1CraftingManager.addShapelessRecipe(new ItemStack(Item.dyePowder, 4, 13), new Object[] {new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 1), new ItemStack(Item.dyePowder, 1, 15)});

        for (i = 0; i < 16; ++i)
        {
            par1CraftingManager.addRecipe(new ItemStack(Block.carpet, 3, i), new Object[] {"##", '#', new ItemStack(Block.cloth, 1, i)});
        }
    }
}
