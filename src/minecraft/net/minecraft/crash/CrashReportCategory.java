package net.minecraft.crash;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.util.MathHelper;

public class CrashReportCategory
{
    private final CrashReport theCrashReport;
    private final String field_85076_b;
    private final List field_85077_c = new ArrayList();
    private StackTraceElement[] stackTrace = new StackTraceElement[0];

    public CrashReportCategory(CrashReport par1CrashReport, String par2Str)
    {
        this.theCrashReport = par1CrashReport;
        this.field_85076_b = par2Str;
    }

    @SideOnly(Side.CLIENT)
    public static String func_85074_a(double par0, double par2, double par4)
    {
        return String.format("%.2f,%.2f,%.2f - %s", new Object[] {Double.valueOf(par0), Double.valueOf(par2), Double.valueOf(par4), getLocationInfo(MathHelper.floor_double(par0), MathHelper.floor_double(par2), MathHelper.floor_double(par4))});
    }

    /**
     * Returns a string with world information on location.Args:x,y,z
     */
    public static String getLocationInfo(int par0, int par1, int par2)
    {
        StringBuilder stringbuilder = new StringBuilder();

        try
        {
            stringbuilder.append(String.format("World: (%d,%d,%d)", new Object[] {Integer.valueOf(par0), Integer.valueOf(par1), Integer.valueOf(par2)}));
        }
        catch (Throwable throwable)
        {
            stringbuilder.append("(Error finding world loc)");
        }

        stringbuilder.append(", ");
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;

        try
        {
            l = par0 >> 4;
            i1 = par2 >> 4;
            j1 = par0 & 15;
            k1 = par1 >> 4;
            l1 = par2 & 15;
            i2 = l << 4;
            j2 = i1 << 4;
            k2 = (l + 1 << 4) - 1;
            l2 = (i1 + 1 << 4) - 1;
            stringbuilder.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] {Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2)}));
        }
        catch (Throwable throwable1)
        {
            stringbuilder.append("(Error finding chunk loc)");
        }

        stringbuilder.append(", ");

        try
        {
            l = par0 >> 9;
            i1 = par2 >> 9;
            j1 = l << 5;
            k1 = i1 << 5;
            l1 = (l + 1 << 5) - 1;
            i2 = (i1 + 1 << 5) - 1;
            j2 = l << 9;
            k2 = i1 << 9;
            l2 = (l + 1 << 9) - 1;
            int i3 = (i1 + 1 << 9) - 1;
            stringbuilder.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] {Integer.valueOf(l), Integer.valueOf(i1), Integer.valueOf(j1), Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(i2), Integer.valueOf(j2), Integer.valueOf(k2), Integer.valueOf(l2), Integer.valueOf(i3)}));
        }
        catch (Throwable throwable2)
        {
            stringbuilder.append("(Error finding world loc)");
        }

        return stringbuilder.toString();
    }

    /**
     * Adds a Crashreport section with the given name with the value set to the result of the given Callable;
     */
    public void addCrashSectionCallable(String par1Str, Callable par2Callable)
    {
        try
        {
            this.addCrashSection(par1Str, par2Callable.call());
        }
        catch (Throwable throwable)
        {
            this.addCrashSectionThrowable(par1Str, throwable);
        }
    }

    /**
     * Adds a Crashreport section with the given name with the given value (convered .toString())
     */
    public void addCrashSection(String par1Str, Object par2Obj)
    {
        this.field_85077_c.add(new CrashReportCategoryEntry(par1Str, par2Obj));
    }

    /**
     * Adds a Crashreport section with the given name with the given Throwable
     */
    public void addCrashSectionThrowable(String par1Str, Throwable par2Throwable)
    {
        this.addCrashSection(par1Str, par2Throwable);
    }

    public int func_85073_a(int par1)
    {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        //BugFix: Causes AIOOB for stacks < 3 + par1
        int len = astacktraceelement.length - 3 - par1;
        if (len <= 0) len = astacktraceelement.length;
        this.stackTrace = new StackTraceElement[len];
        System.arraycopy(astacktraceelement, astacktraceelement.length - len, this.stackTrace, 0, this.stackTrace.length);
        return this.stackTrace.length;
    }

    public boolean func_85069_a(StackTraceElement par1StackTraceElement, StackTraceElement par2StackTraceElement)
    {
        if (this.stackTrace.length != 0 && par1StackTraceElement != null)
        {
            StackTraceElement stacktraceelement2 = this.stackTrace[0];

            if (stacktraceelement2.isNativeMethod() == par1StackTraceElement.isNativeMethod() && stacktraceelement2.getClassName().equals(par1StackTraceElement.getClassName()) && stacktraceelement2.getFileName().equals(par1StackTraceElement.getFileName()) && stacktraceelement2.getMethodName().equals(par1StackTraceElement.getMethodName()))
            {
                if (par2StackTraceElement != null != this.stackTrace.length > 1)
                {
                    return false;
                }
                else if (par2StackTraceElement != null && !this.stackTrace[1].equals(par2StackTraceElement))
                {
                    return false;
                }
                else
                {
                    this.stackTrace[0] = par1StackTraceElement;
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public void func_85070_b(int par1)
    {
        StackTraceElement[] astacktraceelement = new StackTraceElement[this.stackTrace.length - par1];
        System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
        this.stackTrace = astacktraceelement;
    }

    public void func_85072_a(StringBuilder par1StringBuilder)
    {
        par1StringBuilder.append("-- ").append(this.field_85076_b).append(" --\n");
        par1StringBuilder.append("Details:");
        Iterator iterator = this.field_85077_c.iterator();

        while (iterator.hasNext())
        {
            CrashReportCategoryEntry crashreportcategoryentry = (CrashReportCategoryEntry)iterator.next();
            par1StringBuilder.append("\n\t");
            par1StringBuilder.append(crashreportcategoryentry.func_85089_a());
            par1StringBuilder.append(": ");
            par1StringBuilder.append(crashreportcategoryentry.func_85090_b());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0)
        {
            par1StringBuilder.append("\nStacktrace:");
            StackTraceElement[] astacktraceelement = this.stackTrace;
            int i = astacktraceelement.length;

            for (int j = 0; j < i; ++j)
            {
                StackTraceElement stacktraceelement = astacktraceelement[j];
                par1StringBuilder.append("\n\tat ");
                par1StringBuilder.append(stacktraceelement.toString());
            }
        }
    }

    public static void addBlockCrashInfo(CrashReportCategory par0CrashReportCategory, int par1, int par2, int par3, int par4, int par5)
    {
        par0CrashReportCategory.addCrashSectionCallable("Block type", new CallableBlockType(par4));
        par0CrashReportCategory.addCrashSectionCallable("Block data value", new CallableBlockDataValue(par5));
        par0CrashReportCategory.addCrashSectionCallable("Block location", new CallableBlockLocation(par1, par2, par3));
    }
}
