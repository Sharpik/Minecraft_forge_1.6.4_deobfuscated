package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class BlockCommandBlock extends BlockContainer
{
    public BlockCommandBlock(int par1)
    {
        super(par1, Material.iron);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityCommandBlock();
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
            int i1 = par1World.getBlockMetadata(par2, par3, par4);
            boolean flag1 = (i1 & 1) != 0;

            if (flag && !flag1)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 | 1, 4);
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
            }
            else if (!flag && flag1)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 & -2, 4);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);

        if (tileentity != null && tileentity instanceof TileEntityCommandBlock)
        {
            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            tileentitycommandblock.setSignalStrength(tileentitycommandblock.executeCommandOnPowered(par1World));
            par1World.func_96440_m(par2, par3, par4, this.blockID);
        }
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 1;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)par1World.getBlockTileEntity(par2, par3, par4);

        if (tileentitycommandblock != null)
        {
            par5EntityPlayer.displayGUIEditSign(tileentitycommandblock);
        }

        return true;
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
        return tileentity != null && tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileentity).getSignalStrength() : 0;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)par1World.getBlockTileEntity(par2, par3, par4);

        if (par6ItemStack.hasDisplayName())
        {
            tileentitycommandblock.setCommandSenderName(par6ItemStack.getDisplayName());
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
