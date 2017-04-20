package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate
{
    /** The mob type that can trigger this pressure plate. */
    private EnumMobType triggerMobType;

    protected BlockPressurePlate(int par1, String par2Str, Material par3Material, EnumMobType par4EnumMobType)
    {
        super(par1, par2Str, par3Material);
        this.triggerMobType = par4EnumMobType;
    }

    /**
     * Argument is weight (0-15). Return the metadata to be set because of it.
     */
    protected int getMetaFromWeight(int par1)
    {
        return par1 > 0 ? 1 : 0;
    }

    /**
     * Argument is metadata. Returns power level (0-15)
     */
    protected int getPowerSupply(int par1)
    {
        return par1 == 1 ? 15 : 0;
    }

    /**
     * Returns the current state of the pressure plate. Returns a value between 0 and 15 based on the number of items on
     * it.
     */
    protected int getPlateState(World par1World, int par2, int par3, int par4)
    {
        List list = null;

        if (this.triggerMobType == EnumMobType.everything)
        {
            list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, this.getSensitiveAABB(par2, par3, par4));
        }

        if (this.triggerMobType == EnumMobType.mobs)
        {
            list = par1World.getEntitiesWithinAABB(EntityLivingBase.class, this.getSensitiveAABB(par2, par3, par4));
        }

        if (this.triggerMobType == EnumMobType.players)
        {
            list = par1World.getEntitiesWithinAABB(EntityPlayer.class, this.getSensitiveAABB(par2, par3, par4));
        }

        if (list != null && !list.isEmpty())
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();

                if (!entity.doesEntityNotTriggerPressurePlate())
                {
                    return 15;
                }
            }
        }

        return 0;
    }
}
