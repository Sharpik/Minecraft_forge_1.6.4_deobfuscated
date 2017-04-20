package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;
import org.lwjgl.Sys;

@SideOnly(Side.CLIENT)
class CallableLWJGLVersion implements Callable
{
    /** Reference to the Minecraft object. */
    final Minecraft mc;

    CallableLWJGLVersion(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    public String getType()
    {
        return Sys.getVersion();
    }

    public Object call()
    {
        return this.getType();
    }
}
