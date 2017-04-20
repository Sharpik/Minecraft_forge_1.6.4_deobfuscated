package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RequestDelete extends Request
{
    public RequestDelete(String par1Str, int par2, int par3)
    {
        super(par1Str, par2, par3);
    }

    public RequestDelete func_96370_f()
    {
        try
        {
            this.field_96367_a.setDoOutput(true);
            this.field_96367_a.setRequestMethod("DELETE");
            this.field_96367_a.connect();
            return this;
        }
        catch (Exception exception)
        {
            throw new ExceptionMcoHttp("Failed URL: " + this.field_96365_b, exception);
        }
    }

    public Request func_96359_e()
    {
        return this.func_96370_f();
    }
}
