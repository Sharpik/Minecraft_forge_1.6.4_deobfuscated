package net.minecraft.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

final class StatTypeSimple implements IStatType
{
    @SideOnly(Side.CLIENT)

    /**
     * Formats a given stat for human consumption.
     */
    public String format(int par1)
    {
        return StatBase.getNumberFormat().format((long)par1);
    }
}
