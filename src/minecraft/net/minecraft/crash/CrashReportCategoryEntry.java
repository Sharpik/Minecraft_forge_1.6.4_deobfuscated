package net.minecraft.crash;

class CrashReportCategoryEntry
{
    private final String field_85092_a;
    private final String field_85091_b;

    public CrashReportCategoryEntry(String par1Str, Object par2Obj)
    {
        this.field_85092_a = par1Str;

        if (par2Obj == null)
        {
            this.field_85091_b = "~~NULL~~";
        }
        else if (par2Obj instanceof Throwable)
        {
            Throwable throwable = (Throwable)par2Obj;
            this.field_85091_b = "~~ERROR~~ " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
        }
        else
        {
            this.field_85091_b = par2Obj.toString();
        }
    }

    public String func_85089_a()
    {
        return this.field_85092_a;
    }

    public String func_85090_b()
    {
        return this.field_85091_b;
    }
}
