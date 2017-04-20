package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class BlockDropper extends BlockDispenser
{
    private final IBehaviorDispenseItem dropperDefaultBehaviour = new BehaviorDefaultDispenseItem();

    protected BlockDropper(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("furnace_side");
        this.furnaceTopIcon = par1IconRegister.registerIcon("furnace_top");
        this.furnaceFrontIcon = par1IconRegister.registerIcon(this.getTextureName() + "_front_horizontal");
        this.field_96473_e = par1IconRegister.registerIcon(this.getTextureName() + "_front_vertical");
    }

    /**
     * Returns the behavior for the given ItemStack.
     */
    protected IBehaviorDispenseItem getBehaviorForItemStack(ItemStack par1ItemStack)
    {
        return this.dropperDefaultBehaviour;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityDropper();
    }

    protected void dispense(World par1World, int par2, int par3, int par4)
    {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(par1World, par2, par3, par4);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();

        if (tileentitydispenser != null)
        {
            int l = tileentitydispenser.getRandomStackFromInventory();

            if (l < 0)
            {
                par1World.playAuxSFX(1001, par2, par3, par4, 0);
            }
            else
            {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
                int i1 = par1World.getBlockMetadata(par2, par3, par4) & 7;
                IInventory iinventory = TileEntityHopper.getInventoryAtLocation(par1World, (double)(par2 + Facing.offsetsXForSide[i1]), (double)(par3 + Facing.offsetsYForSide[i1]), (double)(par4 + Facing.offsetsZForSide[i1]));
                ItemStack itemstack1;

                if (iinventory != null)
                {
                    itemstack1 = TileEntityHopper.insertStack(iinventory, itemstack.copy().splitStack(1), Facing.oppositeSide[i1]);

                    if (itemstack1 == null)
                    {
                        itemstack1 = itemstack.copy();

                        if (--itemstack1.stackSize == 0)
                        {
                            itemstack1 = null;
                        }
                    }
                    else
                    {
                        itemstack1 = itemstack.copy();
                    }
                }
                else
                {
                    itemstack1 = this.dropperDefaultBehaviour.dispense(blocksourceimpl, itemstack);

                    if (itemstack1 != null && itemstack1.stackSize == 0)
                    {
                        itemstack1 = null;
                    }
                }

                tileentitydispenser.setInventorySlotContents(l, itemstack1);
            }
        }
    }
}
