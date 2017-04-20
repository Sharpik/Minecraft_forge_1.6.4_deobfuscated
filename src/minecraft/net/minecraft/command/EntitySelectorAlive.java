package net.minecraft.command;

import net.minecraft.entity.Entity;

final class EntitySelectorAlive implements IEntitySelector
{
    /**
     * Return whether the specified entity is applicable to this filter.
     */
    public boolean isEntityApplicable(Entity par1Entity)
    {
        return par1Entity.isEntityAlive();
    }
}
