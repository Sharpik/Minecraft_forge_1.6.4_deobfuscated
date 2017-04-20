package net.minecraft.client.main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public final class MainShutdownHook extends Thread
{
    public void run()
    {
        Minecraft.stopIntegratedServer();
    }
}
