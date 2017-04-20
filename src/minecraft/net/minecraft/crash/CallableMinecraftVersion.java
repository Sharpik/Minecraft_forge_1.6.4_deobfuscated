package net.minecraft.crash;

import java.util.concurrent.Callable;

public class CallableMinecraftVersion implements Callable
{
    /** Reference to the CrashReport object. */
    final CrashReport theCrashReport;

    public CallableMinecraftVersion(CrashReport par1CrashReport)
    {
        this.theCrashReport = par1CrashReport;
    }

    /**
     * The current version of Minecraft
     */
    public String minecraftVersion()
    {
        return "1.6.4";
    }

    public Object call()
    {
        return this.minecraftVersion();
    }
}
