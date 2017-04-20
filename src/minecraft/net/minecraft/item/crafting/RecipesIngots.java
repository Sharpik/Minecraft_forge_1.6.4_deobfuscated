package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesIngots
{
    private Object[][] recipeItems;

    public RecipesIngots()
    {
        this.recipeItems = new Object[][] {{Block.blockGold, new ItemStack(Item.ingotGold, 9)}, {Block.blockIron, new ItemStack(Item.ingotIron, 9)}, {Block.blockDiamond, new ItemStack(Item.diamond, 9)}, {Block.blockEmerald, new ItemStack(Item.emerald, 9)}, {Block.blockLapis, new ItemStack(Item.dyePowder, 9, 4)}, {Block.blockRedstone, new ItemStack(Item.redstone, 9)}, {Block.coalBlock, new ItemStack(Item.coal, 9, 0)}, {Block.hay, new ItemStack(Item.wheat, 9)}};
    }

    /**
     * Adds the ingot recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager par1CraftingManager)
    {
        for (int i = 0; i < this.recipeItems.length; ++i)
        {
            Block block = (Block)this.recipeItems[i][0];
            ItemStack itemstack = (ItemStack)this.recipeItems[i][1];
            par1CraftingManager.addRecipe(new ItemStack(block), new Object[] {"###", "###", "###", '#', itemstack});
            par1CraftingManager.addRecipe(itemstack, new Object[] {"#", '#', block});
        }

        par1CraftingManager.addRecipe(new ItemStack(Item.ingotGold), new Object[] {"###", "###", "###", '#', Item.goldNugget});
        par1CraftingManager.addRecipe(new ItemStack(Item.goldNugget, 9), new Object[] {"#", '#', Item.ingotGold});
    }
}
