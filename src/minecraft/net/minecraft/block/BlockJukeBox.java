package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityRecordPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockJukeBox extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private Icon theIcon;

    protected BlockJukeBox(int par1)
    {
        super(par1, Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.theIcon : this.blockIcon;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            return false;
        }
        else
        {
            this.ejectRecord(par1World, par2, par3, par4);
            return true;
        }
    }

    /**
     * Insert the specified music disc in the jukebox at the given coordinates
     */
    public void insertRecord(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack)
    {
        if (!par1World.isRemote)
        {
            TileEntityRecordPlayer tileentityrecordplayer = (TileEntityRecordPlayer)par1World.getBlockTileEntity(par2, par3, par4);

            if (tileentityrecordplayer != null)
            {
                tileentityrecordplayer.func_96098_a(par5ItemStack.copy());
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
            }
        }
    }

    /**
     * Ejects the current record inside of the jukebox.
     */
    public void ejectRecord(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            TileEntityRecordPlayer tileentityrecordplayer = (TileEntityRecordPlayer)par1World.getBlockTileEntity(par2, par3, par4);

            if (tileentityrecordplayer != null)
            {
                ItemStack itemstack = tileentityrecordplayer.func_96097_a();

                if (itemstack != null)
                {
                    par1World.playAuxSFX(1005, par2, par3, par4, 0);
                    par1World.playRecord((String)null, par2, par3, par4);
                    tileentityrecordplayer.func_96098_a((ItemStack)null);
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 2);
                    float f = 0.7F;
                    double d0 = (double)(par1World.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(par1World.rand.nextFloat() * f) + (double)(1.0F - f) * 0.2D + 0.6D;
                    double d2 = (double)(par1World.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    ItemStack itemstack1 = itemstack.copy();
                    EntityItem entityitem = new EntityItem(par1World, (double)par2 + d0, (double)par3 + d1, (double)par4 + d2, itemstack1);
                    entityitem.delayBeforeCanPickup = 10;
                    par1World.spawnEntityInWorld(entityitem);
                }
            }
        }
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        this.ejectRecord(par1World, par2, par3, par4);
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, 0);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityRecordPlayer();
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(this.getTextureName() + "_side");
        this.theIcon = par1IconRegister.registerIcon(this.getTextureName() + "_top");
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
        ItemStack itemstack = ((TileEntityRecordPlayer)par1World.getBlockTileEntity(par2, par3, par4)).func_96097_a();
        return itemstack == null ? 0 : itemstack.itemID + 1 - Item.record13.itemID;
    }
}
