package net.minecraft.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

final class StatTypeDistance implements IStatType
{
    @SideOnly(Side.CLIENT)

    /**
     * Formats a given stat for human consumption.
     */
    public String format(int par1)
    {
        double d0 = (double)par1 / 100.0D;
        double d1 = d0 / 1000.0D;
        return d1 > 0.5D ? StatBase.getDecimalFormat().format(d1) + " km" : (d0 > 0.5D ? StatBase.getDecimalFormat().format(d0) + " m" : par1 + " cm");
    }
}
