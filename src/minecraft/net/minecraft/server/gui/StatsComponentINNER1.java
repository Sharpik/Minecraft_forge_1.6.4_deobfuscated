package net.minecraft.server.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SideOnly(Side.SERVER)
class StatsComponentINNER1 implements ActionListener
{
    final StatsComponent field_120030_a;

    StatsComponentINNER1(StatsComponent par1StatsComponent)
    {
        this.field_120030_a = par1StatsComponent;
    }

    public void actionPerformed(ActionEvent par1ActionEvent)
    {
        StatsComponent.func_120033_a(this.field_120030_a);
    }
}
