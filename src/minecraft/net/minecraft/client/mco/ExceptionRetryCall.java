package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExceptionRetryCall extends ExceptionMcoService
{
    public final int field_96393_c;

    public ExceptionRetryCall(int par1)
    {
        super(503, "Retry operation", -1);
        this.field_96393_c = par1;
    }
}
