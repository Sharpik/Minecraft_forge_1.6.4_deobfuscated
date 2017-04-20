package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockFlowerPot extends Block
{
    public BlockFlowerPot(int par1)
    {
        super(par1, Material.circuits);
        this.setBlockBoundsForItemRender();
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        float f = 0.375F;
        float f1 = f / 2.0F;
        this.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 33;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();

        if (itemstack == null)
        {
            return false;
        }
        else if (par1World.getBlockMetadata(par2, par3, par4) != 0)
        {
            return false;
        }
        else
        {
            int i1 = getMetaForPlant(itemstack);

            if (i1 > 0)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i1, 2);

                if (!par5EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                {
                    par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        ItemStack itemstack = getPlantForMeta(par1World.getBlockMetadata(par2, par3, par4));
        return itemstack == null ? Item.flowerPot.itemID : itemstack.itemID;
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World par1World, int par2, int par3, int par4)
    {
        ItemStack itemstack = getPlantForMeta(par1World.getBlockMetadata(par2, par3, par4));
        return itemstack == null ? Item.flowerPot.itemID : itemstack.getItemDamage();
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot()
    {
        return true;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        if (par5 > 0)
        {
            ItemStack itemstack = getPlantForMeta(par5);

            if (itemstack != null)
            {
                this.dropBlockAsItem_do(par1World, par2, par3, par4, itemstack);
            }
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.flowerPot.itemID;
    }

    /**
     * Return the item associated with the specified flower pot metadata value.
     */
    public static ItemStack getPlantForMeta(int par0)
    {
        switch (par0)
        {
            case 1:
                return new ItemStack(Block.plantRed);
            case 2:
                return new ItemStack(Block.plantYellow);
            case 3:
                return new ItemStack(Block.sapling, 1, 0);
            case 4:
                return new ItemStack(Block.sapling, 1, 1);
            case 5:
                return new ItemStack(Block.sapling, 1, 2);
            case 6:
                return new ItemStack(Block.sapling, 1, 3);
            case 7:
                return new ItemStack(Block.mushroomRed);
            case 8:
                return new ItemStack(Block.mushroomBrown);
            case 9:
                return new ItemStack(Block.cactus);
            case 10:
                return new ItemStack(Block.deadBush);
            case 11:
                return new ItemStack(Block.tallGrass, 1, 2);
            default:
                return null;
        }
    }

    /**
     * Return the flower pot metadata value associated with the specified item.
     */
    public static int getMetaForPlant(ItemStack par0ItemStack)
    {
        int i = par0ItemStack.getItem().itemID;

        if (i == Block.plantRed.blockID)
        {
            return 1;
        }
        else if (i == Block.plantYellow.blockID)
        {
            return 2;
        }
        else if (i == Block.cactus.blockID)
        {
            return 9;
        }
        else if (i == Block.mushroomBrown.blockID)
        {
            return 8;
        }
        else if (i == Block.mushroomRed.blockID)
        {
            return 7;
        }
        else if (i == Block.deadBush.blockID)
        {
            return 10;
        }
        else
        {
            if (i == Block.sapling.blockID)
            {
                switch (par0ItemStack.getItemDamage())
                {
                    case 0:
                        return 3;
                    case 1:
                        return 4;
                    case 2:
                        return 5;
                    case 3:
                        return 6;
                }
            }

            if (i == Block.tallGrass.blockID)
            {
                switch (par0ItemStack.getItemDamage())
                {
                    case 2:
                        return 11;
                }
            }

            return 0;
        }
    }
}
