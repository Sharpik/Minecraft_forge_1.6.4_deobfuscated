package net.minecraft.client.mco;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public abstract class Request
{
    protected HttpURLConnection field_96367_a;
    private boolean field_96366_c;
    protected String field_96365_b;

    public Request(String par1Str, int par2, int par3)
    {
        try
        {
            this.field_96365_b = par1Str;
            this.field_96367_a = (HttpURLConnection)(new URL(par1Str)).openConnection(Minecraft.getMinecraft().getProxy());
            this.field_96367_a.setConnectTimeout(par2);
            this.field_96367_a.setReadTimeout(par3);
        }
        catch (Exception exception)
        {
            throw new ExceptionMcoHttp("Failed URL: " + par1Str, exception);
        }
    }

    public void func_100006_a(String par1Str, String par2Str)
    {
        String s2 = this.field_96367_a.getRequestProperty("Cookie");

        if (s2 == null)
        {
            this.field_96367_a.setRequestProperty("Cookie", par1Str + "=" + par2Str);
        }
        else
        {
            this.field_96367_a.setRequestProperty("Cookie", s2 + ";" + par1Str + "=" + par2Str);
        }
    }

    public int func_96362_a()
    {
        try
        {
            this.func_96354_d();
            return this.field_96367_a.getResponseCode();
        }
        catch (Exception exception)
        {
            throw new ExceptionMcoHttp("Failed URL: " + this.field_96365_b, exception);
        }
    }

    public int func_111221_b()
    {
        String s = this.field_96367_a.getHeaderField("Retry-After");

        try
        {
            return Integer.valueOf(s).intValue();
        }
        catch (Exception exception)
        {
            return 5;
        }
    }

    public String func_96364_c()
    {
        try
        {
            this.func_96354_d();
            String s = this.func_96362_a() >= 400 ? this.func_96352_a(this.field_96367_a.getErrorStream()) : this.func_96352_a(this.field_96367_a.getInputStream());
            this.func_96360_f();
            return s;
        }
        catch (IOException ioexception)
        {
            throw new ExceptionMcoHttp("Failed URL: " + this.field_96365_b, ioexception);
        }
    }

    private String func_96352_a(InputStream par1InputStream) throws IOException
    {
        if (par1InputStream == null)
        {
            throw new IOException("No response (null)");
        }
        else
        {
            StringBuilder stringbuilder = new StringBuilder();

            for (int i = par1InputStream.read(); i != -1; i = par1InputStream.read())
            {
                stringbuilder.append((char)i);
            }

            return stringbuilder.toString();
        }
    }

    private void func_96360_f()
    {
        byte[] abyte = new byte[1024];
        InputStream inputstream;

        try
        {
            boolean flag = false;
            inputstream = this.field_96367_a.getInputStream();

            while (true)
            {
                if (inputstream.read(abyte) <= 0)
                {
                    inputstream.close();
                    break;
                }
            }
        }
        catch (Exception exception)
        {
            try
            {
                inputstream = this.field_96367_a.getErrorStream();
                boolean flag1 = false;

                while (true)
                {
                    if (inputstream.read(abyte) <= 0)
                    {
                        inputstream.close();
                        break;
                    }
                }
            }
            catch (IOException ioexception)
            {
                ;
            }
        }
    }

    protected Request func_96354_d()
    {
        if (!this.field_96366_c)
        {
            Request request = this.func_96359_e();
            this.field_96366_c = true;
            return request;
        }
        else
        {
            return this;
        }
    }

    protected abstract Request func_96359_e();

    public static Request func_96358_a(String par0Str)
    {
        return new RequestGet(par0Str, 5000, 10000);
    }

    public static Request func_96361_b(String par0Str, String par1Str)
    {
        return new RequestPost(par0Str, par1Str.getBytes(), 5000, 10000);
    }

    public static Request func_104064_a(String par0Str, String par1Str, int par2, int par3)
    {
        return new RequestPost(par0Str, par1Str.getBytes(), par2, par3);
    }

    public static Request func_96355_b(String par0Str)
    {
        return new RequestDelete(par0Str, 5000, 10000);
    }

    public static Request func_96363_c(String par0Str, String par1Str)
    {
        return new RequestPut(par0Str, par1Str.getBytes(), 5000, 10000);
    }

    public static Request func_96353_a(String par0Str, String par1Str, int par2, int par3)
    {
        return new RequestPut(par0Str, par1Str.getBytes(), par2, par3);
    }

    public int func_130110_g()
    {
        String s = this.field_96367_a.getHeaderField("Error-Code");

        try
        {
            return Integer.valueOf(s).intValue();
        }
        catch (Exception exception)
        {
            return -1;
        }
    }
}
