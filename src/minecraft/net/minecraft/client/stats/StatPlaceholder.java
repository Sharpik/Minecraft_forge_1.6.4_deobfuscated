package net.minecraft.client.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.stats.StatBase;

@SideOnly(Side.CLIENT)
public class StatPlaceholder extends StatBase
{
    public StatPlaceholder(int par1)
    {
        super(par1, "Unknown stat");
    }
}
