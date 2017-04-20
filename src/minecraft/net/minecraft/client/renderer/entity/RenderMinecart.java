package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMinecart extends Render
{
    private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");

    /** instance of ModelMinecart for rendering */
    protected ModelBase modelMinecart = new ModelMinecart();
    protected final RenderBlocks field_94145_f;

    public RenderMinecart()
    {
        this.shadowSize = 0.5F;
        this.field_94145_f = new RenderBlocks();
    }

    /**
     * Renders the Minecart.
     */
    public void renderTheMinecart(EntityMinecart par1EntityMinecart, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        this.bindEntityTexture(par1EntityMinecart);
        long i = (long)par1EntityMinecart.entityId * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f2 = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f3 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f4 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GL11.glTranslatef(f2, f3, f4);
        double d3 = par1EntityMinecart.lastTickPosX + (par1EntityMinecart.posX - par1EntityMinecart.lastTickPosX) * (double)par9;
        double d4 = par1EntityMinecart.lastTickPosY + (par1EntityMinecart.posY - par1EntityMinecart.lastTickPosY) * (double)par9;
        double d5 = par1EntityMinecart.lastTickPosZ + (par1EntityMinecart.posZ - par1EntityMinecart.lastTickPosZ) * (double)par9;
        double d6 = 0.30000001192092896D;
        Vec3 vec3 = par1EntityMinecart.func_70489_a(d3, d4, d5);
        float f5 = par1EntityMinecart.prevRotationPitch + (par1EntityMinecart.rotationPitch - par1EntityMinecart.prevRotationPitch) * par9;

        if (vec3 != null)
        {
            Vec3 vec31 = par1EntityMinecart.func_70495_a(d3, d4, d5, d6);
            Vec3 vec32 = par1EntityMinecart.func_70495_a(d3, d4, d5, -d6);

            if (vec31 == null)
            {
                vec31 = vec3;
            }

            if (vec32 == null)
            {
                vec32 = vec3;
            }

            par2 += vec3.xCoord - d3;
            par4 += (vec31.yCoord + vec32.yCoord) / 2.0D - d4;
            par6 += vec3.zCoord - d5;
            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);

            if (vec33.lengthVector() != 0.0D)
            {
                vec33 = vec33.normalize();
                par8 = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
                f5 = (float)(Math.atan(vec33.yCoord) * 73.0D);
            }
        }

        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-f5, 0.0F, 0.0F, 1.0F);
        float f6 = (float)par1EntityMinecart.getRollingAmplitude() - par9;
        float f7 = par1EntityMinecart.getDamage() - par9;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            GL11.glRotatef(MathHelper.sin(f6) * f6 * f7 / 10.0F * (float)par1EntityMinecart.getRollingDirection(), 1.0F, 0.0F, 0.0F);
        }

        int j = par1EntityMinecart.getDisplayTileOffset();
        Block block = par1EntityMinecart.getDisplayTile();
        int k = par1EntityMinecart.getDisplayTileData();

        if (block != null)
        {
            GL11.glPushMatrix();
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f8 = 0.75F;
            GL11.glScalef(f8, f8, f8);
            GL11.glTranslatef(0.0F, (float)j / 16.0F, 0.0F);
            this.renderBlockInMinecart(par1EntityMinecart, par9, block, k);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindEntityTexture(par1EntityMinecart);
        }

        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelMinecart.render(par1EntityMinecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getMinecartTextures(EntityMinecart par1EntityMinecart)
    {
        return minecartTextures;
    }

    /**
     * Renders the block that is inside the minecart.
     */
    protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4)
    {
        float f1 = par1EntityMinecart.getBrightness(par2);
        GL11.glPushMatrix();
        this.field_94145_f.renderBlockAsItem(par3Block, par4, f1);
        GL11.glPopMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getMinecartTextures((EntityMinecart)par1Entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderTheMinecart((EntityMinecart)par1Entity, par2, par4, par6, par8, par9);
    }
}
