package net.minecraft.world.storage;

import java.util.concurrent.Callable;

class CallableLevelGeneratorOptions implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelGeneratorOptions(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String callLevelGeneratorOptions()
    {
        return WorldInfo.getWorldGeneratorOptions(this.worldInfoInstance);
    }

    public Object call()
    {
        return this.callLevelGeneratorOptions();
    }
}
