package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableModded implements Callable
{
    /** Reference to the Minecraft object. */
    final Minecraft mc;

    CallableModded(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    /**
     * Gets if Client Profiler (aka Snooper) is enabled.
     */
    public String getClientProfilerEnabled()
    {
        String s = ClientBrandRetriever.getClientModName();
        return !s.equals("vanilla") ? "Definitely; Client brand changed to \'" + s + "\'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.");
    }

    public Object call()
    {
        return this.getClientProfilerEnabled();
    }
}
