package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.util.AxisAlignedBB;

class CallableCrashMemoryReport implements Callable
{
    final CrashReport theCrashReport;

    CallableCrashMemoryReport(CrashReport par1CrashReport)
    {
        this.theCrashReport = par1CrashReport;
    }

    /**
     * Returns a string with allocated and used memory.
     */
    public String getMemoryReport()
    {
        int i = AxisAlignedBB.getAABBPool().getlistAABBsize();
        int j = 56 * i;
        int k = j / 1024 / 1024;
        int l = AxisAlignedBB.getAABBPool().getnextPoolIndex();
        int i1 = 56 * l;
        int j1 = i1 / 1024 / 1024;
        return i + " (" + j + " bytes; " + k + " MB) allocated, " + l + " (" + i1 + " bytes; " + j1 + " MB) used";
    }

    public Object call()
    {
        return this.getMemoryReport();
    }
}
