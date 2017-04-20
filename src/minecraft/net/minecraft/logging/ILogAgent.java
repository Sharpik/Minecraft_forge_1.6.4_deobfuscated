package net.minecraft.logging;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.logging.Logger;

public interface ILogAgent
{
    void logInfo(String s);

    @SideOnly(Side.SERVER)
    Logger func_120013_a();

    void logWarning(String s);

    void logWarningFormatted(String s, Object ... var2);

    void logWarningException(String s, Throwable throwable);

    void logSevere(String s);

    void logSevereException(String s, Throwable throwable);

    @SideOnly(Side.CLIENT)
    void logFine(String s);
}
