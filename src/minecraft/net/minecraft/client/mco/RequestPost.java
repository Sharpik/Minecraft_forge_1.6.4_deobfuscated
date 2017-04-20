package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.OutputStream;

@SideOnly(Side.CLIENT)
public class RequestPost extends Request
{
    private byte[] field_96373_c;

    public RequestPost(String par1Str, byte[] par2ArrayOfByte, int par3, int par4)
    {
        super(par1Str, par3, par4);
        this.field_96373_c = par2ArrayOfByte;
    }

    public RequestPost func_96372_f()
    {
        try
        {
            this.field_96367_a.setDoInput(true);
            this.field_96367_a.setDoOutput(true);
            this.field_96367_a.setUseCaches(false);
            this.field_96367_a.setRequestMethod("POST");
            OutputStream outputstream = this.field_96367_a.getOutputStream();
            outputstream.write(this.field_96373_c);
            outputstream.flush();
            return this;
        }
        catch (Exception exception)
        {
            throw new ExceptionMcoHttp("Failed URL: " + this.field_96365_b, exception);
        }
    }

    public Request func_96359_e()
    {
        return this.func_96372_f();
    }
}
