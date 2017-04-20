package net.minecraft.crash;

import java.util.concurrent.Callable;

class CallableMemoryInfo implements Callable
{
    /** Reference to the CrashReport object. */
    final CrashReport theCrashReport;

    CallableMemoryInfo(CrashReport par1CrashReport)
    {
        this.theCrashReport = par1CrashReport;
    }

    /**
     * Returns the memory information as a String.  Includes the Free Memory in bytes and MB, Total Memory in bytes and
     * MB, and Max Memory in Bytes and MB.
     */
    public String getMemoryInfoAsString()
    {
        Runtime runtime = Runtime.getRuntime();
        long i = runtime.maxMemory();
        long j = runtime.totalMemory();
        long k = runtime.freeMemory();
        long l = i / 1024L / 1024L;
        long i1 = j / 1024L / 1024L;
        long j1 = k / 1024L / 1024L;
        return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
    }

    public Object call()
    {
        return this.getMemoryInfoAsString();
    }
}
