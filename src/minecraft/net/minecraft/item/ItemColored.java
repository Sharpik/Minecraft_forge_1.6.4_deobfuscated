package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class ItemColored extends ItemBlock
{
    private final Block blockRef;
    private String[] blockNames;

    public ItemColored(int par1, boolean par2)
    {
        super(par1);
        this.blockRef = Block.blocksList[this.getBlockID()];

        if (par2)
        {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return this.blockRef.getRenderColor(par1ItemStack.getItemDamage());
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1)
    {
        return par1;
    }

    /**
     * Sets the array of strings to be used for name lookups from item damage to metadata
     */
    public ItemColored setBlockNames(String[] par1ArrayOfStr)
    {
        this.blockNames = par1ArrayOfStr;
        return this;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int par1)
    {
        return this.blockRef.getIcon(0, par1);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (this.blockNames == null)
        {
            return super.getUnlocalizedName(par1ItemStack);
        }
        else
        {
            int i = par1ItemStack.getItemDamage();
            return i >= 0 && i < this.blockNames.length ? super.getUnlocalizedName(par1ItemStack) + "." + this.blockNames[i] : super.getUnlocalizedName(par1ItemStack);
        }
    }
}
