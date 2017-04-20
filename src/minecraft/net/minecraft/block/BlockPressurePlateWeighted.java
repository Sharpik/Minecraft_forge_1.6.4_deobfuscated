package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    /** The maximum number of items the plate weights. */
    private final int maxItemsWeighted;

    protected BlockPressurePlateWeighted(int par1, String par2Str, Material par3Material, int par4)
    {
        super(par1, par2Str, par3Material);
        this.maxItemsWeighted = par4;
    }

    /**
     * Returns the current state of the pressure plate. Returns a value between 0 and 15 based on the number of items on
     * it.
     */
    protected int getPlateState(World par1World, int par2, int par3, int par4)
    {
        int l = 0;
        Iterator iterator = par1World.getEntitiesWithinAABB(EntityItem.class, this.getSensitiveAABB(par2, par3, par4)).iterator();

        while (iterator.hasNext())
        {
            EntityItem entityitem = (EntityItem)iterator.next();
            l += entityitem.getEntityItem().stackSize;

            if (l >= this.maxItemsWeighted)
            {
                break;
            }
        }

        if (l <= 0)
        {
            return 0;
        }
        else
        {
            float f = (float)Math.min(this.maxItemsWeighted, l) / (float)this.maxItemsWeighted;
            return MathHelper.ceiling_float_int(f * 15.0F);
        }
    }

    /**
     * Argument is metadata. Returns power level (0-15)
     */
    protected int getPowerSupply(int par1)
    {
        return par1;
    }

    /**
     * Argument is weight (0-15). Return the metadata to be set because of it.
     */
    protected int getMetaFromWeight(int par1)
    {
        return par1;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 10;
    }
}
