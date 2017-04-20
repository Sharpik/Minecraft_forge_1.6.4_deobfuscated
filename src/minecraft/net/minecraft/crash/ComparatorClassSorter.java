package net.minecraft.crash;

import java.util.Comparator;

class ComparatorClassSorter implements Comparator
{
    final CallableSuspiciousClasses theSuspiciousClasses;

    ComparatorClassSorter(CallableSuspiciousClasses par1CallableSuspiciousClasses)
    {
        this.theSuspiciousClasses = par1CallableSuspiciousClasses;
    }

    public int func_85081_a(Class par1Class, Class par2Class)
    {
        String s = par1Class.getPackage() == null ? "" : par1Class.getPackage().getName();
        String s1 = par2Class.getPackage() == null ? "" : par2Class.getPackage().getName();
        return s.compareTo(s1);
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.func_85081_a((Class)par1Obj, (Class)par2Obj);
    }
}
