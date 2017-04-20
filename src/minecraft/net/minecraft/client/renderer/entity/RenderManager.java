package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderManager
{
    /** A map of entity classes and the associated renderer. */
    public Map entityRenderMap = new HashMap();

    /** The static instance of RenderManager. */
    public static RenderManager instance = new RenderManager();

    /** Renders fonts */
    private FontRenderer fontRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public TextureManager renderEngine;
    public ItemRenderer itemRenderer;

    /** Reference to the World object. */
    public World worldObj;

    /** Rendermanager's variable for the player */
    public EntityLivingBase livingPlayer;
    public EntityLivingBase field_96451_i;
    public float playerViewY;
    public float playerViewX;

    /** Reference to the GameSettings object. */
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    public static boolean field_85095_o;

    private RenderManager()
    {
        this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider());
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
        this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
        this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
        this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
        this.entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
        this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
        this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
        this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish());
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
        this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman());
        this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan());
        this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton());
        this.entityRenderMap.put(EntityWitch.class, new RenderWitch());
        this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze());
        this.entityRenderMap.put(EntityZombie.class, new RenderZombie());
        this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
        this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube());
        this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
        this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
        this.entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
        this.entityRenderMap.put(EntityVillager.class, new RenderVillager());
        this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem());
        this.entityRenderMap.put(EntityBat.class, new RenderBat());
        this.entityRenderMap.put(EntityDragon.class, new RenderDragon());
        this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal());
        this.entityRenderMap.put(EntityWither.class, new RenderWither());
        this.entityRenderMap.put(Entity.class, new RenderEntity());
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
        this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame());
        this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot());
        this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
        this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball));
        this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(Item.enderPearl));
        this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(Item.eyeOfEnder));
        this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg));
        this.entityRenderMap.put(EntityPotion.class, new RenderSnowball(Item.potion, 16384));
        this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(Item.expBottle));
        this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(Item.firework));
        this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(2.0F));
        this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(0.5F));
        this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull());
        this.entityRenderMap.put(EntityItem.class, new RenderItem());
        this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb());
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
        this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
        this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart());
        this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner());
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
        this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
        this.entityRenderMap.put(EntityFishHook.class, new RenderFish());
        this.entityRenderMap.put(EntityHorse.class, new RenderHorse(new ModelHorse(), 0.75F));
        this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());
        Iterator iterator = this.entityRenderMap.values().iterator();

        while (iterator.hasNext())
        {
            Render render = (Render)iterator.next();
            render.setRenderManager(this);
        }
    }

    public Render getEntityClassRenderObject(Class par1Class)
    {
        Render render = (Render)this.entityRenderMap.get(par1Class);

        if (render == null && par1Class != Entity.class)
        {
            render = this.getEntityClassRenderObject(par1Class.getSuperclass());
            this.entityRenderMap.put(par1Class, render);
        }

        return render;
    }

    public Render getEntityRenderObject(Entity par1Entity)
    {
        return this.getEntityClassRenderObject(par1Entity.getClass());
    }

    /**
     * Caches the current frame's active render info, including the current World, RenderEngine, GameSettings and
     * FontRenderer settings, as well as interpolated player position, pitch and yaw.
     */
    public void cacheActiveRenderInfo(World par1World, TextureManager par2TextureManager, FontRenderer par3FontRenderer, EntityLivingBase par4EntityLivingBase, EntityLivingBase par5EntityLivingBase, GameSettings par6GameSettings, float par7)
    {
        this.worldObj = par1World;
        this.renderEngine = par2TextureManager;
        this.options = par6GameSettings;
        this.livingPlayer = par4EntityLivingBase;
        this.field_96451_i = par5EntityLivingBase;
        this.fontRenderer = par3FontRenderer;

        if (par4EntityLivingBase.isPlayerSleeping())
        {
            int x = MathHelper.floor_double(par4EntityLivingBase.posX);
            int y = MathHelper.floor_double(par4EntityLivingBase.posY);
            int z = MathHelper.floor_double(par4EntityLivingBase.posZ);
            Block block = Block.blocksList[par1World.getBlockId(x, y, z)];

            if (block != null && block.isBed(par1World, x, y, z, par4EntityLivingBase))
            {
                int k = block.getBedDirection(par1World, x, y, z);
                this.playerViewY = (float)(k * 90 + 180);
                this.playerViewX = 0.0F;
            }
        }
        else
        {
            this.playerViewY = par4EntityLivingBase.prevRotationYaw + (par4EntityLivingBase.rotationYaw - par4EntityLivingBase.prevRotationYaw) * par7;
            this.playerViewX = par4EntityLivingBase.prevRotationPitch + (par4EntityLivingBase.rotationPitch - par4EntityLivingBase.prevRotationPitch) * par7;
        }

        if (par6GameSettings.thirdPersonView == 2)
        {
            this.playerViewY += 180.0F;
        }

        this.viewerPosX = par4EntityLivingBase.lastTickPosX + (par4EntityLivingBase.posX - par4EntityLivingBase.lastTickPosX) * (double)par7;
        this.viewerPosY = par4EntityLivingBase.lastTickPosY + (par4EntityLivingBase.posY - par4EntityLivingBase.lastTickPosY) * (double)par7;
        this.viewerPosZ = par4EntityLivingBase.lastTickPosZ + (par4EntityLivingBase.posZ - par4EntityLivingBase.lastTickPosZ) * (double)par7;
    }

    /**
     * Will render the specified entity at the specified partial tick time. Args: entity, partialTickTime
     */
    public void renderEntity(Entity par1Entity, float par2)
    {
        if (par1Entity.ticksExisted == 0)
        {
            par1Entity.lastTickPosX = par1Entity.posX;
            par1Entity.lastTickPosY = par1Entity.posY;
            par1Entity.lastTickPosZ = par1Entity.posZ;
        }

        double d0 = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
        double d1 = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
        double d2 = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;
        float f1 = par1Entity.prevRotationYaw + (par1Entity.rotationYaw - par1Entity.prevRotationYaw) * par2;
        int i = par1Entity.getBrightnessForRender(par2);

        if (par1Entity.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderEntityWithPosYaw(par1Entity, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ, f1, par2);
    }

    /**
     * Renders the specified entity with the passed in position, yaw, and partialTickTime. Args: entity, x, y, z, yaw,
     * partialTickTime
     */
    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Render render = null;

        try
        {
            render = this.getEntityRenderObject(par1Entity);

            if (render != null && this.renderEngine != null)
            {
                if (field_85095_o && !par1Entity.isInvisible())
                {
                    try
                    {
                        this.func_85094_b(par1Entity, par2, par4, par6, par8, par9);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }

                try
                {
                    render.doRender(par1Entity, par2, par4, par6, par8, par9);
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
                }

                try
                {
                    render.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Post-rendering entity in world"));
                }
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            par1Entity.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.func_85074_a(par2, par4, par6));
            crashreportcategory1.addCrashSection("Rotation", Float.valueOf(par8));
            crashreportcategory1.addCrashSection("Delta", Float.valueOf(par9));
            throw new ReportedException(crashreport);
        }
    }

    private void func_85094_b(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(255, 255, 255, 32);
        double d3 = (double)(-par1Entity.width / 2.0F);
        double d4 = (double)(-par1Entity.width / 2.0F);
        double d5 = (double)(par1Entity.width / 2.0F);
        double d6 = (double)(-par1Entity.width / 2.0F);
        double d7 = (double)(-par1Entity.width / 2.0F);
        double d8 = (double)(par1Entity.width / 2.0F);
        double d9 = (double)(par1Entity.width / 2.0F);
        double d10 = (double)(par1Entity.width / 2.0F);
        double d11 = (double)par1Entity.height;
        tessellator.addVertex(par2 + d3, par4 + d11, par6 + d4);
        tessellator.addVertex(par2 + d3, par4, par6 + d4);
        tessellator.addVertex(par2 + d5, par4, par6 + d6);
        tessellator.addVertex(par2 + d5, par4 + d11, par6 + d6);
        tessellator.addVertex(par2 + d9, par4 + d11, par6 + d10);
        tessellator.addVertex(par2 + d9, par4, par6 + d10);
        tessellator.addVertex(par2 + d7, par4, par6 + d8);
        tessellator.addVertex(par2 + d7, par4 + d11, par6 + d8);
        tessellator.addVertex(par2 + d5, par4 + d11, par6 + d6);
        tessellator.addVertex(par2 + d5, par4, par6 + d6);
        tessellator.addVertex(par2 + d9, par4, par6 + d10);
        tessellator.addVertex(par2 + d9, par4 + d11, par6 + d10);
        tessellator.addVertex(par2 + d7, par4 + d11, par6 + d8);
        tessellator.addVertex(par2 + d7, par4, par6 + d8);
        tessellator.addVertex(par2 + d3, par4, par6 + d4);
        tessellator.addVertex(par2 + d3, par4 + d11, par6 + d4);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void set(World par1World)
    {
        this.worldObj = par1World;
    }

    public double getDistanceToCamera(double par1, double par3, double par5)
    {
        double d3 = par1 - this.viewerPosX;
        double d4 = par3 - this.viewerPosY;
        double d5 = par5 - this.viewerPosZ;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    /**
     * Returns the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }

    public void updateIcons(IconRegister par1IconRegister)
    {
        Iterator iterator = this.entityRenderMap.values().iterator();

        while (iterator.hasNext())
        {
            Render render = (Render)iterator.next();
            render.updateIcons(par1IconRegister);
        }
    }
}
