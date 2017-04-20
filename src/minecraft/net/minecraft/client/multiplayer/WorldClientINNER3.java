package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class WorldClientINNER3 implements Callable
{
    final WorldClient theWorldClient;

    WorldClientINNER3(WorldClient par1WorldClient)
    {
        this.theWorldClient = par1WorldClient;
    }

    public String func_142026_a()
    {
        return WorldClient.func_142030_c(this.theWorldClient).thePlayer.func_142021_k();
    }

    public Object call()
    {
        return this.func_142026_a();
    }
}
