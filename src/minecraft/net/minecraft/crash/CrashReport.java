package net.minecraft.crash;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.minecraft.logging.ILogAgent;
import net.minecraft.util.ReportedException;

public class CrashReport
{
    /** Description of the crash report. */
    private final String description;

    /** The Throwable that is the "cause" for this crash and Crash Report. */
    private final Throwable cause;
    private final CrashReportCategory field_85061_c = new CrashReportCategory(this, "System Details");

    /** Holds the keys and values of all crash report sections. */
    private final List crashReportSections = new ArrayList();

    /** File of crash report. */
    private File crashReportFile;
    private boolean field_85059_f = true;
    private StackTraceElement[] field_85060_g = new StackTraceElement[0];

    public CrashReport(String par1Str, Throwable par2Throwable)
    {
        this.description = par1Str;
        this.cause = par2Throwable;
        this.populateEnvironment();
    }

    /**
     * Populates this crash report with initial information about the running server and operating system / java
     * environment
     */
    private void populateEnvironment()
    {
        this.field_85061_c.addCrashSectionCallable("Minecraft Version", new CallableMinecraftVersion(this));
        this.field_85061_c.addCrashSectionCallable("Operating System", new CallableOSInfo(this));
        this.field_85061_c.addCrashSectionCallable("Java Version", new CallableJavaInfo(this));
        this.field_85061_c.addCrashSectionCallable("Java VM Version", new CallableJavaInfo2(this));
        this.field_85061_c.addCrashSectionCallable("Memory", new CallableMemoryInfo(this));
        this.field_85061_c.addCrashSectionCallable("JVM Flags", new CallableJVMFlags(this));
        this.field_85061_c.addCrashSectionCallable("AABB Pool Size", new CallableCrashMemoryReport(this));
        this.field_85061_c.addCrashSectionCallable("Suspicious classes", new CallableSuspiciousClasses(this));
        this.field_85061_c.addCrashSectionCallable("IntCache", new CallableIntCache(this));
        FMLCommonHandler.instance().enhanceCrashReport(this, this.field_85061_c);
    }

    /**
     * Returns the description of the Crash Report.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the Throwable object that is the cause for the crash and Crash Report.
     */
    public Throwable getCrashCause()
    {
        return this.cause;
    }

    /**
     * Gets the various sections of the crash report into the given StringBuilder
     */
    public void getSectionsInStringBuilder(StringBuilder par1StringBuilder)
    {
        if (this.field_85060_g != null && this.field_85060_g.length > 0)
        {
            par1StringBuilder.append("-- Head --\n");
            par1StringBuilder.append("Stacktrace:\n");
            StackTraceElement[] astacktraceelement = this.field_85060_g;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j)
            {
                StackTraceElement stacktraceelement = astacktraceelement[j];
                par1StringBuilder.append("\t").append("at ").append(stacktraceelement.toString());
                par1StringBuilder.append("\n");
            }

            par1StringBuilder.append("\n");
        }

        Iterator iterator = this.crashReportSections.iterator();

        while (iterator.hasNext())
        {
            CrashReportCategory crashreportcategory = (CrashReportCategory)iterator.next();
            crashreportcategory.func_85072_a(par1StringBuilder);
            par1StringBuilder.append("\n\n");
        }

        this.field_85061_c.func_85072_a(par1StringBuilder);
    }

    /**
     * Gets the stack trace of the Throwable that caused this crash report, or if that fails, the cause .toString().
     */
    public String getCauseStackTraceOrString()
    {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        String s = this.cause.toString();

        try
        {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            this.cause.printStackTrace(printwriter);
            s = stringwriter.toString();
        }
        finally
        {
            try
            {
                if (stringwriter != null)
                {
                    stringwriter.close();
                }

                if (printwriter != null)
                {
                    printwriter.close();
                }
            }
            catch (IOException ioexception)
            {
                ;
            }
        }

        return s;
    }

    /**
     * Gets the complete report with headers, stack trace, and different sections as a string.
     */
    public String getCompleteReport()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.description);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i)
        {
            stringbuilder.append("-");
        }

        stringbuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the file this crash report is saved into.
     */
    public File getFile()
    {
        return this.crashReportFile;
    }

    /**
     * Saves the complete crash report to the given File.
     */
    public boolean saveToFile(File par1File, ILogAgent par2ILogAgent)
    {
        if (this.crashReportFile != null)
        {
            return false;
        }
        else
        {
            if (par1File.getParentFile() != null)
            {
                par1File.getParentFile().mkdirs();
            }

            try
            {
                FileWriter filewriter = new FileWriter(par1File);
                filewriter.write(this.getCompleteReport());
                filewriter.close();
                this.crashReportFile = par1File;
                return true;
            }
            catch (Throwable throwable)
            {
                par2ILogAgent.logSevereException("Could not save crash report to " + par1File, throwable);
                return false;
            }
        }
    }

    public CrashReportCategory getCategory()
    {
        return this.field_85061_c;
    }

    /**
     * Creates a CrashReportCategory
     */
    public CrashReportCategory makeCategory(String par1Str)
    {
        return this.makeCategoryDepth(par1Str, 1);
    }

    /**
     * Creates a CrashReportCategory for the given stack trace depth
     */
    public CrashReportCategory makeCategoryDepth(String par1Str, int par2)
    {
        CrashReportCategory crashreportcategory = new CrashReportCategory(this, par1Str);

        if (this.field_85059_f)
        {
            int j = crashreportcategory.func_85073_a(par2);
            StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;

            int idx = astacktraceelement.length - j; //Forge fix AIOOB exception.
            if (astacktraceelement != null && idx < astacktraceelement.length && idx >= 0)
            {
                stacktraceelement = astacktraceelement[astacktraceelement.length - j];

                if (astacktraceelement.length + 1 - j < astacktraceelement.length)
                {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - j];
                }
            }

            this.field_85059_f = crashreportcategory.func_85069_a(stacktraceelement, stacktraceelement1);

            if (j > 0 && !this.crashReportSections.isEmpty())
            {
                CrashReportCategory crashreportcategory1 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
                crashreportcategory1.func_85070_b(j);
            }
            else if (astacktraceelement != null && astacktraceelement.length >= j)
            {
                this.field_85060_g = new StackTraceElement[astacktraceelement.length - j];
                System.arraycopy(astacktraceelement, 0, this.field_85060_g, 0, this.field_85060_g.length);
            }
            else
            {
                this.field_85059_f = false;
            }
        }

        this.crashReportSections.add(crashreportcategory);
        return crashreportcategory;
    }

    /**
     * Gets a random witty comment for inclusion in this CrashReport
     */
    private static String getWittyComment()
    {
        String[] astring = new String[] {"Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!"};

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
     * Creates a crash report for the exception
     */
    public static CrashReport makeCrashReport(Throwable par0Throwable, String par1Str)
    {
        CrashReport crashreport;

        if (par0Throwable instanceof ReportedException)
        {
            crashreport = ((ReportedException)par0Throwable).getCrashReport();
        }
        else
        {
            crashreport = new CrashReport(par1Str, par0Throwable);
        }

        return crashreport;
    }
}
