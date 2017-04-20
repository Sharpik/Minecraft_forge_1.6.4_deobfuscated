package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnvilConverterException extends Exception
{
    public AnvilConverterException(String par1Str)
    {
        super(par1Str);
    }
}
