package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureOffset
{
    /** The x coordinate offset of the texture */
    public final int textureOffsetX;

    /** The y coordinate offset of the texture */
    public final int textureOffsetY;

    public TextureOffset(int par1, int par2)
    {
        this.textureOffsetX = par1;
        this.textureOffsetY = par2;
    }
}
