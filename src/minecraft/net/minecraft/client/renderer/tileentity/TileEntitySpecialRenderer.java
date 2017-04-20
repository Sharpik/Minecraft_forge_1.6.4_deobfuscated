package net.minecraft.client.renderer.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public abstract class TileEntitySpecialRenderer
{
    /**
     * The TileEntityRenderer instance associated with this TileEntitySpecialRenderer
     */
    protected TileEntityRenderer tileEntityRenderer;

    public abstract void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f);

    protected void bindTexture(ResourceLocation par1ResourceLocation)
    {
        TextureManager texturemanager = this.tileEntityRenderer.renderEngine;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(par1ResourceLocation);
        }
    }

    /**
     * Associate a TileEntityRenderer with this TileEntitySpecialRenderer
     */
    public void setTileEntityRenderer(TileEntityRenderer par1TileEntityRenderer)
    {
        this.tileEntityRenderer = par1TileEntityRenderer;
    }

    /**
     * Called when the ingame world being rendered changes (e.g. on world -> nether travel) due to using one renderer
     * per tile entity type, rather than instance
     */
    public void onWorldChange(World par1World) {}

    public FontRenderer getFontRenderer()
    {
        return this.tileEntityRenderer.getFontRenderer();
    }
}
