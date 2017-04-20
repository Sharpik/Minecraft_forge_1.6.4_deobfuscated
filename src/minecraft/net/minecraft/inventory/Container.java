package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public abstract class Container
{
    /** the list of all items(stacks) for the corresponding slot */
    public List inventoryItemStacks = new ArrayList();

    /** the list of all slots in the inventory */
    public List inventorySlots = new ArrayList();
    public int windowId;
    @SideOnly(Side.CLIENT)
    private short transactionID;
    private int field_94535_f = -1;
    private int field_94536_g;
    private final Set field_94537_h = new HashSet();

    /**
     * list of all people that need to be notified when this craftinventory changes
     */
    protected List crafters = new ArrayList();
    private Set playerList = new HashSet();

    /**
     * the slot is assumed empty
     */
    protected Slot addSlotToContainer(Slot par1Slot)
    {
        par1Slot.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(par1Slot);
        this.inventoryItemStacks.add((Object)null);
        return par1Slot;
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        if (this.crafters.contains(par1ICrafting))
        {
            throw new IllegalArgumentException("Listener already listening");
        }
        else
        {
            this.crafters.add(par1ICrafting);
            par1ICrafting.sendContainerAndContentsToPlayer(this, this.getInventory());
            this.detectAndSendChanges();
        }
    }

    /**
     * returns a list if itemStacks, for each slot.
     */
    public List getInventory()
    {
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            arraylist.add(((Slot)this.inventorySlots.get(i)).getStack());
        }

        return arraylist;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Remove this crafting listener from the listener list.
     */
    public void removeCraftingFromCrafters(ICrafting par1ICrafting)
    {
        this.crafters.remove(par1ICrafting);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                itemstack1 = itemstack == null ? null : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                for (int j = 0; j < this.crafters.size(); ++j)
                {
                    ((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
                }
            }
        }
    }

    /**
     * enchants the item on the table using the specified slot; also deducts XP from player
     */
    public boolean enchantItem(EntityPlayer par1EntityPlayer, int par2)
    {
        return false;
    }

    public Slot getSlotFromInventory(IInventory par1IInventory, int par2)
    {
        for (int j = 0; j < this.inventorySlots.size(); ++j)
        {
            Slot slot = (Slot)this.inventorySlots.get(j);

            if (slot.isSlotInInventory(par1IInventory, par2))
            {
                return slot;
            }
        }

        return null;
    }

    public Slot getSlot(int par1)
    {
        return (Slot)this.inventorySlots.get(par1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        Slot slot = (Slot)this.inventorySlots.get(par2);
        return slot != null ? slot.getStack() : null;
    }

    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = par4EntityPlayer.inventory;
        int l;
        ItemStack itemstack1;

        if (par3 == 5)
        {
            int i1 = this.field_94536_g;
            this.field_94536_g = func_94532_c(par2);

            if ((i1 != 1 || this.field_94536_g != 2) && i1 != this.field_94536_g)
            {
                this.func_94533_d();
            }
            else if (inventoryplayer.getItemStack() == null)
            {
                this.func_94533_d();
            }
            else if (this.field_94536_g == 0)
            {
                this.field_94535_f = func_94529_b(par2);

                if (func_94528_d(this.field_94535_f))
                {
                    this.field_94536_g = 1;
                    this.field_94537_h.clear();
                }
                else
                {
                    this.func_94533_d();
                }
            }
            else if (this.field_94536_g == 1)
            {
                Slot slot = (Slot)this.inventorySlots.get(par1);

                if (slot != null && func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.field_94537_h.size() && this.canDragIntoSlot(slot))
                {
                    this.field_94537_h.add(slot);
                }
            }
            else if (this.field_94536_g == 2)
            {
                if (!this.field_94537_h.isEmpty())
                {
                    itemstack1 = inventoryplayer.getItemStack().copy();
                    l = inventoryplayer.getItemStack().stackSize;
                    Iterator iterator = this.field_94537_h.iterator();

                    while (iterator.hasNext())
                    {
                        Slot slot1 = (Slot)iterator.next();

                        if (slot1 != null && func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.field_94537_h.size() && this.canDragIntoSlot(slot1))
                        {
                            ItemStack itemstack2 = itemstack1.copy();
                            int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            func_94525_a(this.field_94537_h, this.field_94535_f, itemstack2, j1);

                            if (itemstack2.stackSize > itemstack2.getMaxStackSize())
                            {
                                itemstack2.stackSize = itemstack2.getMaxStackSize();
                            }

                            if (itemstack2.stackSize > slot1.getSlotStackLimit())
                            {
                                itemstack2.stackSize = slot1.getSlotStackLimit();
                            }

                            l -= itemstack2.stackSize - j1;
                            slot1.putStack(itemstack2);
                        }
                    }

                    itemstack1.stackSize = l;

                    if (itemstack1.stackSize <= 0)
                    {
                        itemstack1 = null;
                    }

                    inventoryplayer.setItemStack(itemstack1);
                }

                this.func_94533_d();
            }
            else
            {
                this.func_94533_d();
            }
        }
        else if (this.field_94536_g != 0)
        {
            this.func_94533_d();
        }
        else
        {
            Slot slot2;
            int k1;
            ItemStack itemstack3;

            if ((par3 == 0 || par3 == 1) && (par2 == 0 || par2 == 1))
            {
                if (par1 == -999)
                {
                    if (inventoryplayer.getItemStack() != null && par1 == -999)
                    {
                        if (par2 == 0)
                        {
                            par4EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack());
                            inventoryplayer.setItemStack((ItemStack)null);
                        }

                        if (par2 == 1)
                        {
                            par4EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));

                            if (inventoryplayer.getItemStack().stackSize == 0)
                            {
                                inventoryplayer.setItemStack((ItemStack)null);
                            }
                        }
                    }
                }
                else if (par3 == 1)
                {
                    if (par1 < 0)
                    {
                        return null;
                    }

                    slot2 = (Slot)this.inventorySlots.get(par1);

                    if (slot2 != null && slot2.canTakeStack(par4EntityPlayer))
                    {
                        itemstack1 = this.transferStackInSlot(par4EntityPlayer, par1);

                        if (itemstack1 != null)
                        {
                            l = itemstack1.itemID;
                            itemstack = itemstack1.copy();

                            if (slot2 != null && slot2.getStack() != null && slot2.getStack().itemID == l)
                            {
                                this.retrySlotClick(par1, par2, true, par4EntityPlayer);
                            }
                        }
                    }
                }
                else
                {
                    if (par1 < 0)
                    {
                        return null;
                    }

                    slot2 = (Slot)this.inventorySlots.get(par1);

                    if (slot2 != null)
                    {
                        itemstack1 = slot2.getStack();
                        ItemStack itemstack4 = inventoryplayer.getItemStack();

                        if (itemstack1 != null)
                        {
                            itemstack = itemstack1.copy();
                        }

                        if (itemstack1 == null)
                        {
                            if (itemstack4 != null && slot2.isItemValid(itemstack4))
                            {
                                k1 = par2 == 0 ? itemstack4.stackSize : 1;

                                if (k1 > slot2.getSlotStackLimit())
                                {
                                    k1 = slot2.getSlotStackLimit();
                                }

                                if (itemstack4.stackSize >= k1)
                                {
                                    slot2.putStack(itemstack4.splitStack(k1));
                                }

                                if (itemstack4.stackSize == 0)
                                {
                                    inventoryplayer.setItemStack((ItemStack)null);
                                }
                            }
                        }
                        else if (slot2.canTakeStack(par4EntityPlayer))
                        {
                            if (itemstack4 == null)
                            {
                                k1 = par2 == 0 ? itemstack1.stackSize : (itemstack1.stackSize + 1) / 2;
                                itemstack3 = slot2.decrStackSize(k1);
                                inventoryplayer.setItemStack(itemstack3);

                                if (itemstack1.stackSize == 0)
                                {
                                    slot2.putStack((ItemStack)null);
                                }

                                slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
                            }
                            else if (slot2.isItemValid(itemstack4))
                            {
                                if (itemstack1.itemID == itemstack4.itemID && itemstack1.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4))
                                {
                                    k1 = par2 == 0 ? itemstack4.stackSize : 1;

                                    if (k1 > slot2.getSlotStackLimit() - itemstack1.stackSize)
                                    {
                                        k1 = slot2.getSlotStackLimit() - itemstack1.stackSize;
                                    }

                                    if (k1 > itemstack4.getMaxStackSize() - itemstack1.stackSize)
                                    {
                                        k1 = itemstack4.getMaxStackSize() - itemstack1.stackSize;
                                    }

                                    itemstack4.splitStack(k1);

                                    if (itemstack4.stackSize == 0)
                                    {
                                        inventoryplayer.setItemStack((ItemStack)null);
                                    }

                                    itemstack1.stackSize += k1;
                                }
                                else if (itemstack4.stackSize <= slot2.getSlotStackLimit())
                                {
                                    slot2.putStack(itemstack4);
                                    inventoryplayer.setItemStack(itemstack1);
                                }
                            }
                            else if (itemstack1.itemID == itemstack4.itemID && itemstack4.getMaxStackSize() > 1 && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4))
                            {
                                k1 = itemstack1.stackSize;

                                if (k1 > 0 && k1 + itemstack4.stackSize <= itemstack4.getMaxStackSize())
                                {
                                    itemstack4.stackSize += k1;
                                    itemstack1 = slot2.decrStackSize(k1);

                                    if (itemstack1.stackSize == 0)
                                    {
                                        slot2.putStack((ItemStack)null);
                                    }

                                    slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
                                }
                            }
                        }

                        slot2.onSlotChanged();
                    }
                }
            }
            else if (par3 == 2 && par2 >= 0 && par2 < 9)
            {
                slot2 = (Slot)this.inventorySlots.get(par1);

                if (slot2.canTakeStack(par4EntityPlayer))
                {
                    itemstack1 = inventoryplayer.getStackInSlot(par2);
                    boolean flag = itemstack1 == null || slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack1);
                    k1 = -1;

                    if (!flag)
                    {
                        k1 = inventoryplayer.getFirstEmptyStack();
                        flag |= k1 > -1;
                    }

                    if (slot2.getHasStack() && flag)
                    {
                        itemstack3 = slot2.getStack();
                        inventoryplayer.setInventorySlotContents(par2, itemstack3.copy());

                        if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack1)) && itemstack1 != null)
                        {
                            if (k1 > -1)
                            {
                                inventoryplayer.addItemStackToInventory(itemstack1);
                                slot2.decrStackSize(itemstack3.stackSize);
                                slot2.putStack((ItemStack)null);
                                slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
                            }
                        }
                        else
                        {
                            slot2.decrStackSize(itemstack3.stackSize);
                            slot2.putStack(itemstack1);
                            slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
                        }
                    }
                    else if (!slot2.getHasStack() && itemstack1 != null && slot2.isItemValid(itemstack1))
                    {
                        inventoryplayer.setInventorySlotContents(par2, (ItemStack)null);
                        slot2.putStack(itemstack1);
                    }
                }
            }
            else if (par3 == 3 && par4EntityPlayer.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && par1 >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(par1);

                if (slot2 != null && slot2.getHasStack())
                {
                    itemstack1 = slot2.getStack().copy();
                    itemstack1.stackSize = itemstack1.getMaxStackSize();
                    inventoryplayer.setItemStack(itemstack1);
                }
            }
            else if (par3 == 4 && inventoryplayer.getItemStack() == null && par1 >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(par1);

                if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(par4EntityPlayer))
                {
                    itemstack1 = slot2.decrStackSize(par2 == 0 ? 1 : slot2.getStack().stackSize);
                    slot2.onPickupFromSlot(par4EntityPlayer, itemstack1);
                    par4EntityPlayer.dropPlayerItem(itemstack1);
                }
            }
            else if (par3 == 6 && par1 >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(par1);
                itemstack1 = inventoryplayer.getItemStack();

                if (itemstack1 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(par4EntityPlayer)))
                {
                    l = par2 == 0 ? 0 : this.inventorySlots.size() - 1;
                    k1 = par2 == 0 ? 1 : -1;

                    for (int l1 = 0; l1 < 2; ++l1)
                    {
                        for (int i2 = l; i2 >= 0 && i2 < this.inventorySlots.size() && itemstack1.stackSize < itemstack1.getMaxStackSize(); i2 += k1)
                        {
                            Slot slot3 = (Slot)this.inventorySlots.get(i2);

                            if (slot3.getHasStack() && func_94527_a(slot3, itemstack1, true) && slot3.canTakeStack(par4EntityPlayer) && this.func_94530_a(itemstack1, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize()))
                            {
                                int j2 = Math.min(itemstack1.getMaxStackSize() - itemstack1.stackSize, slot3.getStack().stackSize);
                                ItemStack itemstack5 = slot3.decrStackSize(j2);
                                itemstack1.stackSize += j2;

                                if (itemstack5.stackSize <= 0)
                                {
                                    slot3.putStack((ItemStack)null);
                                }

                                slot3.onPickupFromSlot(par4EntityPlayer, itemstack5);
                            }
                        }
                    }
                }

                this.detectAndSendChanges();
            }
        }

        return itemstack;
    }

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return true;
    }

    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer)
    {
        this.slotClick(par1, par2, 1, par4EntityPlayer);
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        InventoryPlayer inventoryplayer = par1EntityPlayer.inventory;

        if (inventoryplayer.getItemStack() != null)
        {
            par1EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack());
            inventoryplayer.setItemStack((ItemStack)null);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.detectAndSendChanges();
    }

    /**
     * args: slotID, itemStack to put in slot
     */
    public void putStackInSlot(int par1, ItemStack par2ItemStack)
    {
        this.getSlot(par1).putStack(par2ItemStack);
    }

    @SideOnly(Side.CLIENT)

    /**
     * places itemstacks in first x slots, x being aitemstack.lenght
     */
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack)
    {
        for (int i = 0; i < par1ArrayOfItemStack.length; ++i)
        {
            this.getSlot(i).putStack(par1ArrayOfItemStack[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {}

    @SideOnly(Side.CLIENT)

    /**
     * Gets a unique transaction ID. Parameter is unused.
     */
    public short getNextTransactionID(InventoryPlayer par1InventoryPlayer)
    {
        ++this.transactionID;
        return this.transactionID;
    }

    /**
     * NotUsing because adding a player twice is an error
     */
    public boolean isPlayerNotUsingContainer(EntityPlayer par1EntityPlayer)
    {
        return !this.playerList.contains(par1EntityPlayer);
    }

    /**
     * adds or removes the player from the container based on par2
     */
    public void setPlayerIsPresent(EntityPlayer par1EntityPlayer, boolean par2)
    {
        if (par2)
        {
            this.playerList.remove(par1EntityPlayer);
        }
        else
        {
            this.playerList.add(par1EntityPlayer);
        }
    }

    public abstract boolean canInteractWith(EntityPlayer entityplayer);

    /**
     * merges provided ItemStack with the first avaliable one in the container/player inventory
     */
    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        int k = par2;

        if (par4)
        {
            k = par3 - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (par1ItemStack.isStackable())
        {
            while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.itemID == par1ItemStack.itemID && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
                {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;

                    if (l <= par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (par1ItemStack.stackSize > 0)
        {
            if (par4)
            {
                k = par3 - 1;
            }
            else
            {
                k = par2;
            }

            while (!par4 && k < par3 || par4 && k >= par2)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {
                    slot.putStack(par1ItemStack.copy());
                    slot.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }

    public static int func_94529_b(int par0)
    {
        return par0 >> 2 & 3;
    }

    public static int func_94532_c(int par0)
    {
        return par0 & 3;
    }

    @SideOnly(Side.CLIENT)
    public static int func_94534_d(int par0, int par1)
    {
        return par0 & 3 | (par1 & 3) << 2;
    }

    public static boolean func_94528_d(int par0)
    {
        return par0 == 0 || par0 == 1;
    }

    protected void func_94533_d()
    {
        this.field_94536_g = 0;
        this.field_94537_h.clear();
    }

    public static boolean func_94527_a(Slot par0Slot, ItemStack par1ItemStack, boolean par2)
    {
        boolean flag1 = par0Slot == null || !par0Slot.getHasStack();

        if (par0Slot != null && par0Slot.getHasStack() && par1ItemStack != null && par1ItemStack.isItemEqual(par0Slot.getStack()) && ItemStack.areItemStackTagsEqual(par0Slot.getStack(), par1ItemStack))
        {
            int i = par2 ? 0 : par1ItemStack.stackSize;
            flag1 |= par0Slot.getStack().stackSize + i <= par1ItemStack.getMaxStackSize();
        }

        return flag1;
    }

    public static void func_94525_a(Set par0Set, int par1, ItemStack par2ItemStack, int par3)
    {
        switch (par1)
        {
            case 0:
                par2ItemStack.stackSize = MathHelper.floor_float((float)par2ItemStack.stackSize / (float)par0Set.size());
                break;
            case 1:
                par2ItemStack.stackSize = 1;
        }

        par2ItemStack.stackSize += par3;
    }

    /**
     * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
     * the slot can be added to a list of Slots to split the held ItemStack across.
     */
    public boolean canDragIntoSlot(Slot par1Slot)
    {
        return true;
    }

    public static int calcRedstoneFromInventory(IInventory par0IInventory)
    {
        if (par0IInventory == null)
        {
            return 0;
        }
        else
        {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < par0IInventory.getSizeInventory(); ++j)
            {
                ItemStack itemstack = par0IInventory.getStackInSlot(j);

                if (itemstack != null)
                {
                    f += (float)itemstack.stackSize / (float)Math.min(par0IInventory.getInventoryStackLimit(), itemstack.getMaxStackSize());
                    ++i;
                }
            }

            f /= (float)par0IInventory.getSizeInventory();
            return MathHelper.floor_float(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }
}
