package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

final class DispenserBehaviorDye extends BehaviorDefaultDispenseItem
{
    private boolean field_96461_b = true;

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        if (par2ItemStack.getItemDamage() == 15)
        {
            EnumFacing enumfacing = BlockDispenser.getFacing(par1IBlockSource.getBlockMetadata());
            World world = par1IBlockSource.getWorld();
            int i = par1IBlockSource.getXInt() + enumfacing.getFrontOffsetX();
            int j = par1IBlockSource.getYInt() + enumfacing.getFrontOffsetY();
            int k = par1IBlockSource.getZInt() + enumfacing.getFrontOffsetZ();

            if (ItemDye.func_96604_a(par2ItemStack, world, i, j, k))
            {
                if (!world.isRemote)
                {
                    world.playAuxSFX(2005, i, j, k, 0);
                }
            }
            else
            {
                this.field_96461_b = false;
            }

            return par2ItemStack;
        }
        else
        {
            return super.dispenseStack(par1IBlockSource, par2ItemStack);
        }
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        if (this.field_96461_b)
        {
            par1IBlockSource.getWorld().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
        }
        else
        {
            par1IBlockSource.getWorld().playAuxSFX(1001, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
        }
    }
}
