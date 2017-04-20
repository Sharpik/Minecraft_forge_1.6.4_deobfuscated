package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTntMinecart extends RenderMinecart
{
    protected void func_94146_a(EntityMinecartTNT par1EntityMinecartTNT, float par2, Block par3Block, int par4)
    {
        int j = par1EntityMinecartTNT.func_94104_d();

        if (j > -1 && (float)j - par2 + 1.0F < 10.0F)
        {
            float f1 = 1.0F - ((float)j - par2 + 1.0F) / 10.0F;

            if (f1 < 0.0F)
            {
                f1 = 0.0F;
            }

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            f1 *= f1;
            f1 *= f1;
            float f2 = 1.0F + f1 * 0.3F;
            GL11.glScalef(f2, f2, f2);
        }

        super.renderBlockInMinecart(par1EntityMinecartTNT, par2, par3Block, par4);

        if (j > -1 && j / 5 % 2 == 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - ((float)j - par2 + 1.0F) / 100.0F) * 0.8F);
            GL11.glPushMatrix();
            this.field_94145_f.renderBlockAsItem(Block.tnt, 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    /**
     * Renders the block that is inside the minecart.
     */
    protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4)
    {
        this.func_94146_a((EntityMinecartTNT)par1EntityMinecart, par2, par3Block, par4);
    }
}
