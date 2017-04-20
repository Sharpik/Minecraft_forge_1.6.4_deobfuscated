package net.minecraft.client.renderer.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnchantmentTable;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityRenderer
{
    /**
     * A mapping of TileEntitySpecialRenderers used for each TileEntity that has one
     */
    public Map specialRendererMap = new HashMap();

    /** The static instance of TileEntityRenderer */
    public static TileEntityRenderer instance = new TileEntityRenderer();

    /** The FontRenderer instance used by the TileEntityRenderer */
    private FontRenderer fontRenderer;

    /** The player's current X position (same as playerX) */
    public static double staticPlayerX;

    /** The player's current Y position (same as playerY) */
    public static double staticPlayerY;

    /** The player's current Z position (same as playerZ) */
    public static double staticPlayerZ;

    /** The RenderEngine instance used by the TileEntityRenderer */
    public TextureManager renderEngine;

    /** Reference to the World object. */
    public World worldObj;
    public EntityLivingBase entityLivingPlayer;
    public float playerYaw;
    public float playerPitch;

    /** The player's X position in this rendering context */
    public double playerX;

    /** The player's Y position in this rendering context */
    public double playerY;

    /** The player's Z position in this rendering context */
    public double playerZ;

    private TileEntityRenderer()
    {
        this.specialRendererMap.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.specialRendererMap.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.specialRendererMap.put(TileEntityPiston.class, new TileEntityRendererPiston());
        this.specialRendererMap.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.specialRendererMap.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.specialRendererMap.put(TileEntityEnchantmentTable.class, new RenderEnchantmentTable());
        this.specialRendererMap.put(TileEntityEndPortal.class, new RenderEndPortal());
        this.specialRendererMap.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.specialRendererMap.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        Iterator iterator = this.specialRendererMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();
            tileentityspecialrenderer.setTileEntityRenderer(this);
        }
    }

    /**
     * Returns the TileEntitySpecialRenderer used to render this TileEntity class, or null if it has no special renderer
     */
    public TileEntitySpecialRenderer getSpecialRendererForClass(Class par1Class)
    {
        TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)this.specialRendererMap.get(par1Class);

        if (tileentityspecialrenderer == null && par1Class != TileEntity.class)
        {
            tileentityspecialrenderer = this.getSpecialRendererForClass(par1Class.getSuperclass());
            this.specialRendererMap.put(par1Class, tileentityspecialrenderer);
        }

        return tileentityspecialrenderer;
    }

    /**
     * Returns true if this TileEntity instance has a TileEntitySpecialRenderer associated with it, false otherwise.
     */
    public boolean hasSpecialRenderer(TileEntity par1TileEntity)
    {
        return this.getSpecialRendererForEntity(par1TileEntity) != null;
    }

    /**
     * Returns the TileEntitySpecialRenderer used to render this TileEntity instance, or null if it has no special
     * renderer
     */
    public TileEntitySpecialRenderer getSpecialRendererForEntity(TileEntity par1TileEntity)
    {
        return par1TileEntity == null ? null : this.getSpecialRendererForClass(par1TileEntity.getClass());
    }

    /**
     * Caches several render-related references, including the active World, RenderEngine, FontRenderer, and the camera-
     * bound EntityLiving's interpolated pitch, yaw and position. Args: world, renderengine, fontrenderer, entityliving,
     * partialTickTime
     */
    public void cacheActiveRenderInfo(World par1World, TextureManager par2TextureManager, FontRenderer par3FontRenderer, EntityLivingBase par4EntityLivingBase, float par5)
    {
        if (this.worldObj != par1World)
        {
            this.setWorld(par1World);
        }

        this.renderEngine = par2TextureManager;
        this.entityLivingPlayer = par4EntityLivingBase;
        this.fontRenderer = par3FontRenderer;
        this.playerYaw = par4EntityLivingBase.prevRotationYaw + (par4EntityLivingBase.rotationYaw - par4EntityLivingBase.prevRotationYaw) * par5;
        this.playerPitch = par4EntityLivingBase.prevRotationPitch + (par4EntityLivingBase.rotationPitch - par4EntityLivingBase.prevRotationPitch) * par5;
        this.playerX = par4EntityLivingBase.lastTickPosX + (par4EntityLivingBase.posX - par4EntityLivingBase.lastTickPosX) * (double)par5;
        this.playerY = par4EntityLivingBase.lastTickPosY + (par4EntityLivingBase.posY - par4EntityLivingBase.lastTickPosY) * (double)par5;
        this.playerZ = par4EntityLivingBase.lastTickPosZ + (par4EntityLivingBase.posZ - par4EntityLivingBase.lastTickPosZ) * (double)par5;
    }

    /**
     * Render this TileEntity at its current position from the player
     */
    public void renderTileEntity(TileEntity par1TileEntity, float par2)
    {
        if (par1TileEntity.getDistanceFrom(this.playerX, this.playerY, this.playerZ) < par1TileEntity.getMaxRenderDistanceSquared())
        {
            int i = this.worldObj.getLightBrightnessForSkyBlocks(par1TileEntity.xCoord, par1TileEntity.yCoord, par1TileEntity.zCoord, 0);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderTileEntityAt(par1TileEntity, (double)par1TileEntity.xCoord - staticPlayerX, (double)par1TileEntity.yCoord - staticPlayerY, (double)par1TileEntity.zCoord - staticPlayerZ, par2);
        }
    }

    /**
     * Render this TileEntity at a given set of coordinates
     */
    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        TileEntitySpecialRenderer tileentityspecialrenderer = this.getSpecialRendererForEntity(par1TileEntity);

        if (tileentityspecialrenderer != null)
        {
            try
            {
                tileentityspecialrenderer.renderTileEntityAt(par1TileEntity, par2, par4, par6, par8);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Tile Entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Tile Entity Details");
                par1TileEntity.func_85027_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }

    /**
     * Sets the world used by all TileEntitySpecialRender instances and notifies them of this change.
     */
    public void setWorld(World par1World)
    {
        this.worldObj = par1World;
        Iterator iterator = this.specialRendererMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();

            if (tileentityspecialrenderer != null)
            {
                tileentityspecialrenderer.onWorldChange(par1World);
            }
        }
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }
}
