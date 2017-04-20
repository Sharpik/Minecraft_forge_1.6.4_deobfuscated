package net.minecraft.entity.monster;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals
{
    /** Entity selector for IMob types. */
    IEntitySelector mobSelector = new FilterIMob();
}
