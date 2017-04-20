package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.URI;

@SideOnly(Side.CLIENT)
public class GuiButtonLink extends GuiButton
{
    public GuiButtonLink(int par1, int par2, int par3, int par4, int par5, String par6Str)
    {
        super(par1, par2, par3, par4, par5, par6Str);
    }

    public void func_96135_a(String par1Str)
    {
        try
        {
            URI uri = new URI(par1Str);
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {uri});
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}
