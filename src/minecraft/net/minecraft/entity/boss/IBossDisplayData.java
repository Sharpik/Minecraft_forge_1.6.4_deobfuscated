package net.minecraft.entity.boss;

public interface IBossDisplayData
{
    float getMaxHealth();

    float getHealth();

    /**
     * Gets the username of the entity.
     */
    String getEntityName();
}
