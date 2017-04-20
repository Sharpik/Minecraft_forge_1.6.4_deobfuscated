package net.minecraft.logging;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAgent implements ILogAgent
{
    private final Logger serverLogger;
    private final String logFile;
    private final String loggerName;
    private final String loggerPrefix;

    public LogAgent(String par1Str, String par2Str, String par3Str)
    {
        this.serverLogger = Logger.getLogger(par1Str);
        this.loggerName = par1Str;
        this.loggerPrefix = par2Str;
        this.logFile = par3Str;
        this.setupLogger();
    }

    /**
     * Sets up the logger for usage.
     */
    private void setupLogger()
    {
        this.serverLogger.setParent(FMLLog.getLogger());
        Handler[] ahandler = this.serverLogger.getHandlers();
        int i = ahandler.length;

        for (int j = 0; j < i; ++j)
        {
            Handler handler = ahandler[j];
            this.serverLogger.removeHandler(handler);
        }

        LogFormatter logformatter = new LogFormatter(this, (LogAgentEmptyAnon)null);

        try
        {
            FileHandler filehandler = new FileHandler(this.logFile, true);
            filehandler.setFormatter(logformatter);
            this.serverLogger.addHandler(filehandler);
        }
        catch (Exception exception)
        {
            this.serverLogger.log(Level.WARNING, "Failed to log " + this.loggerName + " to " + this.logFile, exception);
        }
    }

    public void logInfo(String par1Str)
    {
        this.serverLogger.log(Level.INFO, par1Str);
    }

    @SideOnly(Side.SERVER)
    public Logger func_120013_a()
    {
        return this.serverLogger;
    }

    public void logWarning(String par1Str)
    {
        this.serverLogger.log(Level.WARNING, par1Str);
    }

    public void logWarningFormatted(String par1Str, Object ... par2ArrayOfObj)
    {
        this.serverLogger.log(Level.WARNING, String.format(par1Str, par2ArrayOfObj));
    }

    public void logWarningException(String par1Str, Throwable par2Throwable)
    {
        this.serverLogger.log(Level.WARNING, par1Str, par2Throwable);
    }

    public void logSevere(String par1Str)
    {
        this.serverLogger.log(Level.SEVERE, par1Str);
    }

    public void logSevereException(String par1Str, Throwable par2Throwable)
    {
        this.serverLogger.log(Level.SEVERE, par1Str, par2Throwable);
    }

    @SideOnly(Side.CLIENT)
    public void logFine(String par1Str)
    {
        this.serverLogger.log(Level.FINE, par1Str);
    }

    static String func_98237_a(LogAgent par0LogAgent)
    {
        return par0LogAgent.loggerPrefix;
    }
}
