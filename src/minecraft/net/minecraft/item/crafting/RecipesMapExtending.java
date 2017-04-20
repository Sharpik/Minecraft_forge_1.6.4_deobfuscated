package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class RecipesMapExtending extends ShapedRecipes
{
    public RecipesMapExtending()
    {
        super(3, 3, new ItemStack[] {new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.map, 0, 32767), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper), new ItemStack(Item.paper)}, new ItemStack(Item.emptyMap, 0, 0));
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
    {
        if (!super.matches(par1InventoryCrafting, par2World))
        {
            return false;
        }
        else
        {
            ItemStack itemstack = null;

            for (int i = 0; i < par1InventoryCrafting.getSizeInventory() && itemstack == null; ++i)
            {
                ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);

                if (itemstack1 != null && itemstack1.itemID == Item.map.itemID)
                {
                    itemstack = itemstack1;
                }
            }

            if (itemstack == null)
            {
                return false;
            }
            else
            {
                MapData mapdata = Item.map.getMapData(itemstack, par2World);
                return mapdata == null ? false : mapdata.scale < 4;
            }
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        ItemStack itemstack = null;

        for (int i = 0; i < par1InventoryCrafting.getSizeInventory() && itemstack == null; ++i)
        {
            ItemStack itemstack1 = par1InventoryCrafting.getStackInSlot(i);

            if (itemstack1 != null && itemstack1.itemID == Item.map.itemID)
            {
                itemstack = itemstack1;
            }
        }

        itemstack = itemstack.copy();
        itemstack.stackSize = 1;

        if (itemstack.getTagCompound() == null)
        {
            itemstack.setTagCompound(new NBTTagCompound());
        }

        itemstack.getTagCompound().setBoolean("map_is_scaling", true);
        return itemstack;
    }
}
