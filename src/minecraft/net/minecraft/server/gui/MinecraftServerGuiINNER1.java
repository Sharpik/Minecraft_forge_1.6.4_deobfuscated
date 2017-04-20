package net.minecraft.server.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import net.minecraft.server.dedicated.DedicatedServer;

@SideOnly(Side.SERVER)
final class MinecraftServerGuiINNER1 extends WindowAdapter
{
    final DedicatedServer field_120023_a;

    MinecraftServerGuiINNER1(DedicatedServer par1DedicatedServer)
    {
        this.field_120023_a = par1DedicatedServer;
    }

    public void windowClosing(WindowEvent par1WindowEvent)
    {
        this.field_120023_a.initiateShutdown();

        while (!this.field_120023_a.isServerStopped())
        {
            try
            {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }

        System.exit(0);
    }
}
