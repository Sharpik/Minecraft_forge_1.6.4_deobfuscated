package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;

public class ContainerBeacon extends Container
{
    private TileEntityBeacon theBeacon;

    /**
     * This beacon's slot where you put in Emerald, Diamond, Gold or Iron Ingot.
     */
    private final SlotBeacon beaconSlot;
    private int field_82865_g;
    private int field_82867_h;
    private int field_82868_i;

    public ContainerBeacon(InventoryPlayer par1InventoryPlayer, TileEntityBeacon par2TileEntityBeacon)
    {
        this.theBeacon = par2TileEntityBeacon;
        this.addSlotToContainer(this.beaconSlot = new SlotBeacon(this, par2TileEntityBeacon, 0, 136, 110));
        byte b0 = 36;
        short short1 = 137;
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, b0 + j * 18, short1 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, b0 + i * 18, 58 + short1));
        }

        this.field_82865_g = par2TileEntityBeacon.getLevels();
        this.field_82867_h = par2TileEntityBeacon.getPrimaryEffect();
        this.field_82868_i = par2TileEntityBeacon.getSecondaryEffect();
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.field_82865_g);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.field_82867_h);
        par1ICrafting.sendProgressBarUpdate(this, 2, this.field_82868_i);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.theBeacon.setLevels(par2);
        }

        if (par1 == 1)
        {
            this.theBeacon.setPrimaryEffect(par2);
        }

        if (par1 == 2)
        {
            this.theBeacon.setSecondaryEffect(par2);
        }
    }

    /**
     * Returns the Tile Entity behind this beacon inventory / container
     */
    public TileEntityBeacon getBeacon()
    {
        return this.theBeacon;
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.theBeacon.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0)
            {
                if (!this.mergeItemStack(itemstack1, 1, 37, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack1) && itemstack1.stackSize == 1)
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return null;
                }
            }
            else if (par2 >= 1 && par2 < 28)
            {
                if (!this.mergeItemStack(itemstack1, 28, 37, false))
                {
                    return null;
                }
            }
            else if (par2 >= 28 && par2 < 37)
            {
                if (!this.mergeItemStack(itemstack1, 1, 28, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 1, 37, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
