package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class MinecraftINNER13 implements Callable
{
    final Minecraft field_142056_a;

    MinecraftINNER13(Minecraft par1Minecraft)
    {
        this.field_142056_a = par1Minecraft;
    }

    public String func_142055_a()
    {
        int i = this.field_142056_a.theWorld.getWorldVec3Pool().getPoolSize();
        int j = 56 * i;
        int k = j / 1024 / 1024;
        int l = this.field_142056_a.theWorld.getWorldVec3Pool().func_82590_d();
        int i1 = 56 * l;
        int j1 = i1 / 1024 / 1024;
        return i + " (" + j + " bytes; " + k + " MB) allocated, " + l + " (" + i1 + " bytes; " + j1 + " MB) used";
    }

    public Object call()
    {
        return this.func_142055_a();
    }
}
