package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private Entity indirectEntity;

    public EntityDamageSourceIndirect(String par1Str, Entity par2Entity, Entity par3Entity)
    {
        super(par1Str, par2Entity);
        this.indirectEntity = par3Entity;
    }

    public Entity getSourceOfDamage()
    {
        return this.damageSourceEntity;
    }

    public Entity getEntity()
    {
        return this.indirectEntity;
    }

    /**
     * Returns the message to be displayed on player death.
     */
    public ChatMessageComponent getDeathMessage(EntityLivingBase par1EntityLivingBase)
    {
        String s = this.indirectEntity == null ? this.damageSourceEntity.getTranslatedEntityName() : this.indirectEntity.getTranslatedEntityName();
        ItemStack itemstack = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
        String s1 = "death.attack." + this.damageType;
        String s2 = s1 + ".item";
        return itemstack != null && itemstack.hasDisplayName() && StatCollector.func_94522_b(s2) ? ChatMessageComponent.createFromTranslationWithSubstitutions(s2, new Object[] {par1EntityLivingBase.getTranslatedEntityName(), s, itemstack.getDisplayName()}): ChatMessageComponent.createFromTranslationWithSubstitutions(s1, new Object[] {par1EntityLivingBase.getTranslatedEntityName(), s});
    }
}
