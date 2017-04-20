package net.minecraft.command;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.profiler.ProfilerResult;
import net.minecraft.server.MinecraftServer;

public class CommandDebug extends CommandBase
{
    /** Time the debugging started in milliseconds. */
    private long startTime;

    /** The number of ticks when debugging started. */
    private int startTicks;

    public String getCommandName()
    {
        return "debug";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "commands.debug.usage";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            if (par2ArrayOfStr[0].equals("start"))
            {
                notifyAdmins(par1ICommandSender, "commands.debug.start", new Object[0]);
                MinecraftServer.getServer().enableProfiling();
                this.startTime = MinecraftServer.getSystemTimeMillis();
                this.startTicks = MinecraftServer.getServer().getTickCounter();
                return;
            }

            if (par2ArrayOfStr[0].equals("stop"))
            {
                if (!MinecraftServer.getServer().theProfiler.profilingEnabled)
                {
                    throw new CommandException("commands.debug.notStarted", new Object[0]);
                }

                long i = MinecraftServer.getSystemTimeMillis();
                int j = MinecraftServer.getServer().getTickCounter();
                long k = i - this.startTime;
                int l = j - this.startTicks;
                this.saveProfilerResults(k, l);
                MinecraftServer.getServer().theProfiler.profilingEnabled = false;
                notifyAdmins(par1ICommandSender, "commands.debug.stop", new Object[] {Float.valueOf((float)k / 1000.0F), Integer.valueOf(l)});
                return;
            }
        }

        throw new WrongUsageException("commands.debug.usage", new Object[0]);
    }

    private void saveProfilerResults(long par1, int par3)
    {
        File file1 = new File(MinecraftServer.getServer().getFile("debug"), "profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
        file1.getParentFile().mkdirs();

        try
        {
            FileWriter filewriter = new FileWriter(file1);
            filewriter.write(this.getProfilerResults(par1, par3));
            filewriter.close();
        }
        catch (Throwable throwable)
        {
            MinecraftServer.getServer().getLogAgent().logSevereException("Could not save profiler results to " + file1, throwable);
        }
    }

    private String getProfilerResults(long par1, int par3)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Profiler Results ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time span: ").append(par1).append(" ms\n");
        stringbuilder.append("Tick span: ").append(par3).append(" ticks\n");
        stringbuilder.append("// This is approximately ").append(String.format("%.2f", new Object[] {Float.valueOf((float)par3 / ((float)par1 / 1000.0F))})).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.getProfileDump(0, "root", stringbuilder);
        stringbuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringbuilder.toString();
    }

    private void getProfileDump(int par1, String par2Str, StringBuilder par3StringBuilder)
    {
        List list = MinecraftServer.getServer().theProfiler.getProfilingData(par2Str);

        if (list != null && list.size() >= 3)
        {
            for (int j = 1; j < list.size(); ++j)
            {
                ProfilerResult profilerresult = (ProfilerResult)list.get(j);
                par3StringBuilder.append(String.format("[%02d] ", new Object[] {Integer.valueOf(par1)}));

                for (int k = 0; k < par1; ++k)
                {
                    par3StringBuilder.append(" ");
                }

                par3StringBuilder.append(profilerresult.field_76331_c);
                par3StringBuilder.append(" - ");
                par3StringBuilder.append(String.format("%.2f", new Object[] {Double.valueOf(profilerresult.field_76332_a)}));
                par3StringBuilder.append("%/");
                par3StringBuilder.append(String.format("%.2f", new Object[] {Double.valueOf(profilerresult.field_76330_b)}));
                par3StringBuilder.append("%\n");

                if (!profilerresult.field_76331_c.equals("unspecified"))
                {
                    try
                    {
                        this.getProfileDump(par1 + 1, par2Str + "." + profilerresult.field_76331_c, par3StringBuilder);
                    }
                    catch (Exception exception)
                    {
                        par3StringBuilder.append("[[ EXCEPTION " + exception + " ]]");
                    }
                }
            }
        }
    }

    /**
     * Returns a random "witty" comment.
     */
    private static String getWittyComment()
    {
        String[] astring = new String[] {"Shiny numbers!", "Am I not running fast enough? :(", "I\'m working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it\'ll have more motivation to work faster! Poor server."};

        try
        {
            return astring[(int)(System.nanoTime() % (long)astring.length)];
        }
        catch (Throwable throwable)
        {
            return "Witty comment unavailable :(";
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"start", "stop"}): null;
    }
}
