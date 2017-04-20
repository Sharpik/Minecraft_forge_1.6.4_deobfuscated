package net.minecraft.client.renderer.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityRendererPiston extends TileEntitySpecialRenderer
{
    /** instance of RenderBlocks used to draw the piston base and extension. */
    private RenderBlocks blockRenderer;

    public void renderPiston(TileEntityPiston par1TileEntityPiston, double par2, double par4, double par6, float par8)
    {
        Block block = Block.blocksList[par1TileEntityPiston.getStoredBlockID()];

        if (block != null && par1TileEntityPiston.getProgress(par8) < 1.0F)
        {
            Tessellator tessellator = Tessellator.instance;
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);

            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }
            else
            {
                GL11.glShadeModel(GL11.GL_FLAT);
            }

            tessellator.startDrawingQuads();
            tessellator.setTranslation((double)((float)par2 - (float)par1TileEntityPiston.xCoord + par1TileEntityPiston.getOffsetX(par8)), (double)((float)par4 - (float)par1TileEntityPiston.yCoord + par1TileEntityPiston.getOffsetY(par8)), (double)((float)par6 - (float)par1TileEntityPiston.zCoord + par1TileEntityPiston.getOffsetZ(par8)));
            tessellator.setColorOpaque(1, 1, 1);

            if (block == Block.pistonExtension && par1TileEntityPiston.getProgress(par8) < 0.5F)
            {
                this.blockRenderer.renderPistonExtensionAllFaces(block, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord, false);
            }
            else if (par1TileEntityPiston.shouldRenderHead() && !par1TileEntityPiston.isExtending())
            {
                Block.pistonExtension.setHeadTexture(((BlockPistonBase)block).getPistonExtensionTexture());
                this.blockRenderer.renderPistonExtensionAllFaces(Block.pistonExtension, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord, par1TileEntityPiston.getProgress(par8) < 0.5F);
                Block.pistonExtension.clearHeadTexture();
                tessellator.setTranslation((double)((float)par2 - (float)par1TileEntityPiston.xCoord), (double)((float)par4 - (float)par1TileEntityPiston.yCoord), (double)((float)par6 - (float)par1TileEntityPiston.zCoord));
                this.blockRenderer.renderPistonBaseAllFaces(block, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord);
            }
            else
            {
                this.blockRenderer.renderBlockAllFaces(block, par1TileEntityPiston.xCoord, par1TileEntityPiston.yCoord, par1TileEntityPiston.zCoord);
            }

            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    /**
     * Called when the ingame world being rendered changes (e.g. on world -> nether travel) due to using one renderer
     * per tile entity type, rather than instance
     */
    public void onWorldChange(World par1World)
    {
        this.blockRenderer = new RenderBlocks(par1World);
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderPiston((TileEntityPiston)par1TileEntity, par2, par4, par6, par8);
    }
}
