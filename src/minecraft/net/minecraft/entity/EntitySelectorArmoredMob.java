package net.minecraft.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntitySelectorArmoredMob implements IEntitySelector
{
    private final ItemStack field_96567_c;

    public EntitySelectorArmoredMob(ItemStack par1ItemStack)
    {
        this.field_96567_c = par1ItemStack;
    }

    /**
     * Return whether the specified entity is applicable to this filter.
     */
    public boolean isEntityApplicable(Entity par1Entity)
    {
        if (!par1Entity.isEntityAlive())
        {
            return false;
        }
        else if (!(par1Entity instanceof EntityLivingBase))
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)par1Entity;
            return entitylivingbase.getCurrentItemOrArmor(EntityLiving.getArmorPosition(this.field_96567_c)) != null ? false : (entitylivingbase instanceof EntityLiving ? ((EntityLiving)entitylivingbase).canPickUpLoot() : entitylivingbase instanceof EntityPlayer);
        }
    }
}
