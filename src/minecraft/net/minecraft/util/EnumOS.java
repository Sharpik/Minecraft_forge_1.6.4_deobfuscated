package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumOS
{
    LINUX,
    SOLARIS,
    WINDOWS,
    MACOS,
    UNKNOWN;
}
