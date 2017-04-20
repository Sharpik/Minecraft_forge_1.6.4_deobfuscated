package net.minecraft.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

final class StatTypeFloat implements IStatType
{
    @SideOnly(Side.CLIENT)

    /**
     * Formats a given stat for human consumption.
     */
    public String format(int par1)
    {
        return StatBase.getDecimalFormat().format((double)par1 * 0.1D);
    }
}
