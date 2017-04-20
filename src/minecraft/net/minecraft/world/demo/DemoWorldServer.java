package net.minecraft.world.demo;

import net.minecraft.logging.ILogAgent;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;

public class DemoWorldServer extends WorldServer
{
    private static final long demoWorldSeed = (long)"North Carolina".hashCode();
    public static final WorldSettings demoWorldSettings = (new WorldSettings(demoWorldSeed, EnumGameType.SURVIVAL, true, false, WorldType.DEFAULT)).enableBonusChest();

    public DemoWorldServer(MinecraftServer par1MinecraftServer, ISaveHandler par2ISaveHandler, String par3Str, int par4, Profiler par5Profiler, ILogAgent par6ILogAgent)
    {
        super(par1MinecraftServer, par2ISaveHandler, par3Str, par4, demoWorldSettings, par5Profiler, par6ILogAgent);
    }
}
