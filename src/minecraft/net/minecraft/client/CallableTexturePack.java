package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableTexturePack implements Callable
{
    final Minecraft theMinecraft;

    CallableTexturePack(Minecraft par1Minecraft)
    {
        this.theMinecraft = par1Minecraft;
    }

    public String callTexturePack()
    {
        return this.theMinecraft.gameSettings.skin;
    }

    public Object call()
    {
        return this.callTexturePack();
    }
}
