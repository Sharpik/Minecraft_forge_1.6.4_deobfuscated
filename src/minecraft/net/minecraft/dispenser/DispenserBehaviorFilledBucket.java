package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

final class DispenserBehaviorFilledBucket extends BehaviorDefaultDispenseItem
{
    private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        ItemBucket itembucket = (ItemBucket)par2ItemStack.getItem();
        int i = par1IBlockSource.getXInt();
        int j = par1IBlockSource.getYInt();
        int k = par1IBlockSource.getZInt();
        EnumFacing enumfacing = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());

        if (itembucket.tryPlaceContainedLiquid(par1IBlockSource.getWorld(), i + enumfacing.getFrontOffsetX(), j + enumfacing.getFrontOffsetY(), k + enumfacing.getFrontOffsetZ()))
        {
            par2ItemStack.itemID = Item.bucketEmpty.itemID;
            par2ItemStack.stackSize = 1;
            return par2ItemStack;
        }
        else
        {
            return this.defaultDispenserItemBehavior.dispense(par1IBlockSource, par2ItemStack);
        }
    }
}
