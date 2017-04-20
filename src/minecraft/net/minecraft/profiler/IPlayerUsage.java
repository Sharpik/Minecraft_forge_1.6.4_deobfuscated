package net.minecraft.profiler;

import net.minecraft.logging.ILogAgent;

public interface IPlayerUsage
{
    void addServerStatsToSnooper(PlayerUsageSnooper playerusagesnooper);

    void addServerTypeToSnooper(PlayerUsageSnooper playerusagesnooper);

    /**
     * Returns whether snooping is enabled or not.
     */
    boolean isSnooperEnabled();

    ILogAgent getLogAgent();
}
