package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraftforge.common.ForgeHooks;

@SideOnly(Side.CLIENT)
public class GuiIngame extends Gui
{
    protected static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    protected static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    protected static final RenderItem itemRenderer = new RenderItem();
    protected final Random rand = new Random();
    protected final Minecraft mc;

    /** ChatGUI instance that retains all previous chat data */
    protected final GuiNewChat persistantChatGUI;
    protected int updateCounter;

    /** The string specifying which record music is playing */
    protected String recordPlaying = "";

    /** How many ticks the record playing message will be displayed */
    protected int recordPlayingUpFor;
    protected boolean recordIsPlaying;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    public float prevVignetteBrightness = 1.0F;

    /** Remaining ticks the item highlight should be visible */
    protected int remainingHighlightTicks;

    /** The ItemStack that is currently being highlighted */
    protected ItemStack highlightingItemStack;

    public GuiIngame(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
        this.persistantChatGUI = new GuiNewChat(par1Minecraft);
    }

    /**
     * Render the ingame overlay with quick icon bar, ...
     */
    public void renderGameOverlay(float par1, boolean par2, int par3, int par4)
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = this.mc.fontRenderer;
        this.mc.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);

        if (Minecraft.isFancyGraphicsEnabled())
        {
            this.renderVignette(this.mc.thePlayer.getBrightness(par1), k, l);
        }
        else
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() != null)
        {
            if (itemstack.itemID == Block.pumpkin.blockID)
            {
                this.renderPumpkinBlur(k, l);
            }
            else
            {
                itemstack.getItem().renderHelmetOverlay(itemstack, mc.thePlayer, scaledresolution, par1, par2, par3, par4);
            }
        }

        if (!this.mc.thePlayer.isPotionActive(Potion.confusion))
        {
            float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

            if (f1 > 0.0F)
            {
                this.func_130015_b(f1, k, l);
            }
        }

        int i1;
        int j1;
        int k1;

        if (!this.mc.playerController.enableEverythingIsScrewedUpMode())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
            this.zLevel = -90.0F;
            this.drawTexturedModalRect(k / 2 - 91, l - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(k / 2 - 91 - 1 + inventoryplayer.currentItem * 20, l - 22 - 1, 0, 22, 24, 22);
            this.mc.getTextureManager().bindTexture(icons);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            this.drawTexturedModalRect(k / 2 - 7, l / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            this.mc.mcProfiler.startSection("bossHealth");
            this.renderBossHealth();
            this.mc.mcProfiler.endSection();

            if (this.mc.playerController.shouldDrawHUD())
            {
                this.func_110327_a(k, l);
            }

            GL11.glDisable(GL11.GL_BLEND);
            this.mc.mcProfiler.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (i1 = 0; i1 < 9; ++i1)
            {
                j1 = k / 2 - 90 + i1 * 20 + 2;
                k1 = l - 16 - 3;
                this.renderInventorySlot(i1, j1, k1, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            this.mc.mcProfiler.endSection();
        }

        int l1;

        if (this.mc.thePlayer.getSleepTimer() > 0)
        {
            this.mc.mcProfiler.startSection("sleep");
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            l1 = this.mc.thePlayer.getSleepTimer();
            float f2 = (float)l1 / 100.0F;

            if (f2 > 1.0F)
            {
                f2 = 1.0F - (float)(l1 - 100) / 10.0F;
            }

            j1 = (int)(220.0F * f2) << 24 | 1052704;
            drawRect(0, 0, k, l, j1);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            this.mc.mcProfiler.endSection();
        }

        l1 = 16777215;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        i1 = k / 2 - 91;
        int i2;
        int j2;
        int k2;
        int l2;
        float f3;
        short short1;

        if (this.mc.thePlayer.isRidingHorse())
        {
            this.mc.mcProfiler.startSection("jumpBar");
            this.mc.getTextureManager().bindTexture(Gui.icons);
            f3 = this.mc.thePlayer.getHorseJumpPower();
            short1 = 182;
            i2 = (int)(f3 * (float)(short1 + 1));
            j2 = l - 32 + 3;
            this.drawTexturedModalRect(i1, j2, 0, 84, short1, 5);

            if (i2 > 0)
            {
                this.drawTexturedModalRect(i1, j2, 0, 89, i2, 5);
            }

            this.mc.mcProfiler.endSection();
        }
        else if (this.mc.playerController.func_78763_f())
        {
            this.mc.mcProfiler.startSection("expBar");
            this.mc.getTextureManager().bindTexture(Gui.icons);
            j1 = this.mc.thePlayer.xpBarCap();

            if (j1 > 0)
            {
                short1 = 182;
                i2 = (int)(this.mc.thePlayer.experience * (float)(short1 + 1));
                j2 = l - 32 + 3;
                this.drawTexturedModalRect(i1, j2, 0, 64, short1, 5);

                if (i2 > 0)
                {
                    this.drawTexturedModalRect(i1, j2, 0, 69, i2, 5);
                }
            }

            this.mc.mcProfiler.endSection();

            if (this.mc.thePlayer.experienceLevel > 0)
            {
                this.mc.mcProfiler.startSection("expLevel");
                boolean flag1 = false;
                i2 = flag1 ? 16777215 : 8453920;
                String s = "" + this.mc.thePlayer.experienceLevel;
                k2 = (k - fontrenderer.getStringWidth(s)) / 2;
                l2 = l - 31 - 4;
                boolean flag2 = false;
                fontrenderer.drawString(s, k2 + 1, l2, 0);
                fontrenderer.drawString(s, k2 - 1, l2, 0);
                fontrenderer.drawString(s, k2, l2 + 1, 0);
                fontrenderer.drawString(s, k2, l2 - 1, 0);
                fontrenderer.drawString(s, k2, l2, i2);
                this.mc.mcProfiler.endSection();
            }
        }

        String s1;

        if (this.mc.gameSettings.heldItemTooltips)
        {
            this.mc.mcProfiler.startSection("toolHighlight");

            if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null)
            {
                s1 = this.highlightingItemStack.getDisplayName();
                k1 = (k - fontrenderer.getStringWidth(s1)) / 2;
                i2 = l - 59;

                if (!this.mc.playerController.shouldDrawHUD())
                {
                    i2 += 14;
                }

                j2 = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);

                if (j2 > 255)
                {
                    j2 = 255;
                }

                if (j2 > 0)
                {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    fontrenderer.drawStringWithShadow(s1, k1, i2, 16777215 + (j2 << 24));
                    FontRenderer font = highlightingItemStack.getItem().getFontRenderer(highlightingItemStack);
                    if (font != null)
                    {
                        k1 = (k - font.getStringWidth(s1)) / 2;
                        font.drawStringWithShadow(s1, k1, i2, 16777215 + (j2 << 24));
                    }
                    else
                    {
                        fontrenderer.drawStringWithShadow(s1, k1, i2, 16777215 + (j2 << 24));
                    }
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glPopMatrix();
                }
            }

            this.mc.mcProfiler.endSection();
        }

        if (this.mc.isDemo())
        {
            this.mc.mcProfiler.startSection("demo");
            s1 = "";

            if (this.mc.theWorld.getTotalWorldTime() >= 120500L)
            {
                s1 = I18n.getString("demo.demoExpired");
            }
            else
            {
                s1 = I18n.getStringParams("demo.remainingTime", new Object[] {StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime()))});
            }

            k1 = fontrenderer.getStringWidth(s1);
            fontrenderer.drawStringWithShadow(s1, k - k1 - 10, 5, 16777215);
            this.mc.mcProfiler.endSection();
        }

        int i3;
        int j3;
        int k3;

        if (this.mc.gameSettings.showDebugInfo)
        {
            this.mc.mcProfiler.startSection("debug");
            GL11.glPushMatrix();
            fontrenderer.drawStringWithShadow("Minecraft 1.6.4 (" + this.mc.debug + ")", 2, 2, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.getWorldProviderName(), 2, 42, 16777215);
            long l3 = Runtime.getRuntime().maxMemory();
            long i4 = Runtime.getRuntime().totalMemory();
            long j4 = Runtime.getRuntime().freeMemory();
            long k4 = i4 - j4;
            String s2 = "Used memory: " + k4 * 100L / l3 + "% (" + k4 / 1024L / 1024L + "MB) of " + l3 / 1024L / 1024L + "MB";
            i3 = 14737632;
            this.drawString(fontrenderer, s2, k - fontrenderer.getStringWidth(s2) - 2, 2, 14737632);
            s2 = "Allocated memory: " + i4 * 100L / l3 + "% (" + i4 / 1024L / 1024L + "MB)";
            this.drawString(fontrenderer, s2, k - fontrenderer.getStringWidth(s2) - 2, 12, 14737632);
            j3 = MathHelper.floor_double(this.mc.thePlayer.posX);
            k3 = MathHelper.floor_double(this.mc.thePlayer.posY);
            int l4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
            this.drawString(fontrenderer, String.format("x: %.5f (%d) // c: %d (%d)", new Object[] {Double.valueOf(this.mc.thePlayer.posX), Integer.valueOf(j3), Integer.valueOf(j3 >> 4), Integer.valueOf(j3 & 15)}), 2, 64, 14737632);
            this.drawString(fontrenderer, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[] {Double.valueOf(this.mc.thePlayer.boundingBox.minY), Double.valueOf(this.mc.thePlayer.posY)}), 2, 72, 14737632);
            this.drawString(fontrenderer, String.format("z: %.5f (%d) // c: %d (%d)", new Object[] {Double.valueOf(this.mc.thePlayer.posZ), Integer.valueOf(l4), Integer.valueOf(l4 >> 4), Integer.valueOf(l4 & 15)}), 2, 80, 14737632);
            int i5 = MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            this.drawString(fontrenderer, "f: " + i5 + " (" + Direction.directions[i5] + ") / " + MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw), 2, 88, 14737632);

            if (this.mc.theWorld != null && this.mc.theWorld.blockExists(j3, k3, l4))
            {
                Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(j3, l4);
                this.drawString(fontrenderer, "lc: " + (chunk.getTopFilledSegment() + 15) + " b: " + chunk.getBiomeGenForWorldCoords(j3 & 15, l4 & 15, this.mc.theWorld.getWorldChunkManager()).biomeName + " bl: " + chunk.getSavedLightValue(EnumSkyBlock.Block, j3 & 15, k3, l4 & 15) + " sl: " + chunk.getSavedLightValue(EnumSkyBlock.Sky, j3 & 15, k3, l4 & 15) + " rl: " + chunk.getBlockLightValue(j3 & 15, k3, l4 & 15, 0), 2, 96, 14737632);
            }

            this.drawString(fontrenderer, String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[] {Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(this.mc.thePlayer.onGround), Integer.valueOf(this.mc.theWorld.getHeightValue(j3, l4))}), 2, 104, 14737632);
            GL11.glPopMatrix();
            this.mc.mcProfiler.endSection();
        }

        if (this.recordPlayingUpFor > 0)
        {
            this.mc.mcProfiler.startSection("overlayMessage");
            f3 = (float)this.recordPlayingUpFor - par1;
            k1 = (int)(f3 * 255.0F / 20.0F);

            if (k1 > 255)
            {
                k1 = 255;
            }

            if (k1 > 8)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(k / 2), (float)(l - 68), 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                i2 = 16777215;

                if (this.recordIsPlaying)
                {
                    i2 = Color.HSBtoRGB(f3 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                fontrenderer.drawString(this.recordPlaying, -fontrenderer.getStringWidth(this.recordPlaying) / 2, -4, i2 + (k1 << 24 & -16777216));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().func_96539_a(1);

        if (scoreobjective != null)
        {
            this.func_96136_a(scoreobjective, l, k, fontrenderer);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, (float)(l - 48), 0.0F);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GL11.glPopMatrix();
        scoreobjective = this.mc.theWorld.getScoreboard().func_96539_a(0);

        if (this.mc.gameSettings.keyBindPlayerList.pressed && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.playerInfoList.size() > 1 || scoreobjective != null))
        {
            this.mc.mcProfiler.startSection("playerList");
            NetClientHandler netclienthandler = this.mc.thePlayer.sendQueue;
            List list = netclienthandler.playerInfoList;
            j2 = netclienthandler.currentServerMaxPlayers;
            k2 = j2;

            for (l2 = 1; k2 > 20; k2 = (j2 + l2 - 1) / l2)
            {
                ++l2;
            }

            int j5 = 300 / l2;

            if (j5 > 150)
            {
                j5 = 150;
            }

            int k5 = (k - l2 * j5) / 2;
            byte b0 = 10;
            drawRect(k5 - 1, b0 - 1, k5 + j5 * l2, b0 + 9 * k2, Integer.MIN_VALUE);

            for (i3 = 0; i3 < j2; ++i3)
            {
                j3 = k5 + i3 % l2 * j5;
                k3 = b0 + i3 / l2 * 9;
                drawRect(j3, k3, j3 + j5 - 1, k3 + 8, 553648127);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (i3 < list.size())
                {
                    GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(i3);
                    ScorePlayerTeam scoreplayerteam = this.mc.theWorld.getScoreboard().getPlayersTeam(guiplayerinfo.name);
                    String s3 = ScorePlayerTeam.formatPlayerName(scoreplayerteam, guiplayerinfo.name);
                    fontrenderer.drawStringWithShadow(s3, j3, k3, 16777215);

                    if (scoreobjective != null)
                    {
                        int l5 = j3 + fontrenderer.getStringWidth(s3) + 5;
                        int i6 = j3 + j5 - 12 - 5;

                        if (i6 - l5 > 5)
                        {
                            Score score = scoreobjective.getScoreboard().func_96529_a(guiplayerinfo.name, scoreobjective);
                            String s4 = EnumChatFormatting.YELLOW + "" + score.getScorePoints();
                            fontrenderer.drawStringWithShadow(s4, i6 - fontrenderer.getStringWidth(s4), k3, 16777215);
                        }
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.mc.getTextureManager().bindTexture(icons);
                    byte b1 = 0;
                    boolean flag3 = false;
                    byte b2;

                    if (guiplayerinfo.responseTime < 0)
                    {
                        b2 = 5;
                    }
                    else if (guiplayerinfo.responseTime < 150)
                    {
                        b2 = 0;
                    }
                    else if (guiplayerinfo.responseTime < 300)
                    {
                        b2 = 1;
                    }
                    else if (guiplayerinfo.responseTime < 600)
                    {
                        b2 = 2;
                    }
                    else if (guiplayerinfo.responseTime < 1000)
                    {
                        b2 = 3;
                    }
                    else
                    {
                        b2 = 4;
                    }

                    this.zLevel += 100.0F;
                    this.drawTexturedModalRect(j3 + j5 - 12, k3, 0 + b1 * 10, 176 + b2 * 8, 10, 8);
                    this.zLevel -= 100.0F;
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    protected void func_96136_a(ScoreObjective par1ScoreObjective, int par2, int par3, FontRenderer par4FontRenderer)
    {
        Scoreboard scoreboard = par1ScoreObjective.getScoreboard();
        Collection collection = scoreboard.func_96534_i(par1ScoreObjective);

        if (collection.size() <= 15)
        {
            int k = par4FontRenderer.getStringWidth(par1ScoreObjective.getDisplayName());
            String s;

            for (Iterator iterator = collection.iterator(); iterator.hasNext(); k = Math.max(k, par4FontRenderer.getStringWidth(s)))
            {
                Score score = (Score)iterator.next();
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            }

            int l = collection.size() * par4FontRenderer.FONT_HEIGHT;
            int i1 = par2 / 2 + l / 3;
            byte b0 = 3;
            int j1 = par3 - k - b0;
            int k1 = 0;
            Iterator iterator1 = collection.iterator();

            while (iterator1.hasNext())
            {
                Score score1 = (Score)iterator1.next();
                ++k1;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
                int l1 = i1 - k1 * par4FontRenderer.FONT_HEIGHT;
                int i2 = par3 - b0 + 2;
                drawRect(j1 - 2, l1, i2, l1 + par4FontRenderer.FONT_HEIGHT, 1342177280);
                par4FontRenderer.drawString(s1, j1, l1, 553648127);
                par4FontRenderer.drawString(s2, i2 - par4FontRenderer.getStringWidth(s2), l1, 553648127);

                if (k1 == collection.size())
                {
                    String s3 = par1ScoreObjective.getDisplayName();
                    drawRect(j1 - 2, l1 - par4FontRenderer.FONT_HEIGHT - 1, i2, l1 - 1, 1610612736);
                    drawRect(j1 - 2, l1 - 1, i2, l1, 1342177280);
                    par4FontRenderer.drawString(s3, j1 + k / 2 - par4FontRenderer.getStringWidth(s3) / 2, l1 - par4FontRenderer.FONT_HEIGHT, 553648127);
                }
            }
        }
    }

    protected void func_110327_a(int par1, int par2)
    {
        boolean flag = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

        if (this.mc.thePlayer.hurtResistantTime < 10)
        {
            flag = false;
        }

        int k = MathHelper.ceiling_float_int(this.mc.thePlayer.getHealth());
        int l = MathHelper.ceiling_float_int(this.mc.thePlayer.prevHealth);
        this.rand.setSeed((long)(this.updateCounter * 312871));
        boolean flag1 = false;
        FoodStats foodstats = this.mc.thePlayer.getFoodStats();
        int i1 = foodstats.getFoodLevel();
        int j1 = foodstats.getPrevFoodLevel();
        AttributeInstance attributeinstance = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        int k1 = par1 / 2 - 91;
        int l1 = par1 / 2 + 91;
        int i2 = par2 - 39;
        float f = (float)attributeinstance.getAttributeValue();
        float f1 = this.mc.thePlayer.getAbsorptionAmount();
        int j2 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
        int k2 = Math.max(10 - (j2 - 2), 3);
        int l2 = i2 - (j2 - 1) * k2 - 10;
        float f2 = f1;
        int i3 = ForgeHooks.getTotalArmorValue(mc.thePlayer);
        int j3 = -1;

        if (this.mc.thePlayer.isPotionActive(Potion.regeneration))
        {
            j3 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
        }

        this.mc.mcProfiler.startSection("armor");
        int k3;
        int l3;

        for (k3 = 0; k3 < 10; ++k3)
        {
            if (i3 > 0)
            {
                l3 = k1 + k3 * 8;

                if (k3 * 2 + 1 < i3)
                {
                    this.drawTexturedModalRect(l3, l2, 34, 9, 9, 9);
                }

                if (k3 * 2 + 1 == i3)
                {
                    this.drawTexturedModalRect(l3, l2, 25, 9, 9, 9);
                }

                if (k3 * 2 + 1 > i3)
                {
                    this.drawTexturedModalRect(l3, l2, 16, 9, 9, 9);
                }
            }
        }

        this.mc.mcProfiler.endStartSection("health");
        int i4;
        int j4;
        int k4;

        for (k3 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; k3 >= 0; --k3)
        {
            l3 = 16;

            if (this.mc.thePlayer.isPotionActive(Potion.poison))
            {
                l3 += 36;
            }
            else if (this.mc.thePlayer.isPotionActive(Potion.wither))
            {
                l3 += 72;
            }

            byte b0 = 0;

            if (flag)
            {
                b0 = 1;
            }

            i4 = MathHelper.ceiling_float_int((float)(k3 + 1) / 10.0F) - 1;
            j4 = k1 + k3 % 10 * 8;
            k4 = i2 - i4 * k2;

            if (k <= 4)
            {
                k4 += this.rand.nextInt(2);
            }

            if (k3 == j3)
            {
                k4 -= 2;
            }

            byte b1 = 0;

            if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
            {
                b1 = 5;
            }

            this.drawTexturedModalRect(j4, k4, 16 + b0 * 9, 9 * b1, 9, 9);

            if (flag)
            {
                if (k3 * 2 + 1 < l)
                {
                    this.drawTexturedModalRect(j4, k4, l3 + 54, 9 * b1, 9, 9);
                }

                if (k3 * 2 + 1 == l)
                {
                    this.drawTexturedModalRect(j4, k4, l3 + 63, 9 * b1, 9, 9);
                }
            }

            if (f2 > 0.0F)
            {
                if (f2 == f1 && f1 % 2.0F == 1.0F)
                {
                    this.drawTexturedModalRect(j4, k4, l3 + 153, 9 * b1, 9, 9);
                }
                else
                {
                    this.drawTexturedModalRect(j4, k4, l3 + 144, 9 * b1, 9, 9);
                }

                f2 -= 2.0F;
            }
            else
            {
                if (k3 * 2 + 1 < k)
                {
                    this.drawTexturedModalRect(j4, k4, l3 + 36, 9 * b1, 9, 9);
                }

                if (k3 * 2 + 1 == k)
                {
                    this.drawTexturedModalRect(j4, k4, l3 + 45, 9 * b1, 9, 9);
                }
            }
        }

        Entity entity = this.mc.thePlayer.ridingEntity;
        int l4;

        if (entity == null)
        {
            this.mc.mcProfiler.endStartSection("food");

            for (l3 = 0; l3 < 10; ++l3)
            {
                l4 = i2;
                i4 = 16;
                byte b2 = 0;

                if (this.mc.thePlayer.isPotionActive(Potion.hunger))
                {
                    i4 += 36;
                    b2 = 13;
                }

                if (this.mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (i1 * 3 + 1) == 0)
                {
                    l4 = i2 + (this.rand.nextInt(3) - 1);
                }

                if (flag1)
                {
                    b2 = 1;
                }

                k4 = l1 - l3 * 8 - 9;
                this.drawTexturedModalRect(k4, l4, 16 + b2 * 9, 27, 9, 9);

                if (flag1)
                {
                    if (l3 * 2 + 1 < j1)
                    {
                        this.drawTexturedModalRect(k4, l4, i4 + 54, 27, 9, 9);
                    }

                    if (l3 * 2 + 1 == j1)
                    {
                        this.drawTexturedModalRect(k4, l4, i4 + 63, 27, 9, 9);
                    }
                }

                if (l3 * 2 + 1 < i1)
                {
                    this.drawTexturedModalRect(k4, l4, i4 + 36, 27, 9, 9);
                }

                if (l3 * 2 + 1 == i1)
                {
                    this.drawTexturedModalRect(k4, l4, i4 + 45, 27, 9, 9);
                }
            }
        }
        else if (entity instanceof EntityLivingBase)
        {
            this.mc.mcProfiler.endStartSection("mountHealth");
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            l4 = (int)Math.ceil((double)entitylivingbase.getHealth());
            float f3 = entitylivingbase.getMaxHealth();
            j4 = (int)(f3 + 0.5F) / 2;

            if (j4 > 30)
            {
                j4 = 30;
            }

            k4 = i2;

            for (int i5 = 0; j4 > 0; i5 += 20)
            {
                int j5 = Math.min(j4, 10);
                j4 -= j5;

                for (int k5 = 0; k5 < j5; ++k5)
                {
                    byte b3 = 52;
                    byte b4 = 0;

                    if (flag1)
                    {
                        b4 = 1;
                    }

                    int l5 = l1 - k5 * 8 - 9;
                    this.drawTexturedModalRect(l5, k4, b3 + b4 * 9, 9, 9, 9);

                    if (k5 * 2 + 1 + i5 < l4)
                    {
                        this.drawTexturedModalRect(l5, k4, b3 + 36, 9, 9, 9);
                    }

                    if (k5 * 2 + 1 + i5 == l4)
                    {
                        this.drawTexturedModalRect(l5, k4, b3 + 45, 9, 9, 9);
                    }
                }

                k4 -= 10;
            }
        }

        this.mc.mcProfiler.endStartSection("air");

        if (this.mc.thePlayer.isInsideOfMaterial(Material.water))
        {
            l3 = this.mc.thePlayer.getAir();
            l4 = MathHelper.ceiling_double_int((double)(l3 - 2) * 10.0D / 300.0D);
            i4 = MathHelper.ceiling_double_int((double)l3 * 10.0D / 300.0D) - l4;

            for (j4 = 0; j4 < l4 + i4; ++j4)
            {
                if (j4 < l4)
                {
                    this.drawTexturedModalRect(l1 - j4 * 8 - 9, l2, 16, 18, 9, 9);
                }
                else
                {
                    this.drawTexturedModalRect(l1 - j4 * 8 - 9, l2, 25, 18, 9, 9);
                }
            }
        }

        this.mc.mcProfiler.endSection();
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    protected void renderBossHealth()
    {
        if (BossStatus.bossName != null && BossStatus.statusBarLength > 0)
        {
            --BossStatus.statusBarLength;
            FontRenderer fontrenderer = this.mc.fontRenderer;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            short short1 = 182;
            int j = i / 2 - short1 / 2;
            int k = (int)(BossStatus.healthScale * (float)(short1 + 1));
            byte b0 = 12;
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

            if (k > 0)
            {
                this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
            }

            String s = BossStatus.bossName;
            fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, b0 - 10, 16777215);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    protected void renderPumpkinBlur(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, (double)par2, -90.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)par1, (double)par2, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)par1, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the vignette. Args: vignetteBrightness, width, height
     */
    protected void renderVignette(float par1, int par2, int par3)
    {
        par1 = 1.0F - par1;

        if (par1 < 0.0F)
        {
            par1 = 0.0F;
        }

        if (par1 > 1.0F)
        {
            par1 = 1.0F;
        }

        this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(par1 - this.prevVignetteBrightness) * 0.01D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        this.mc.getTextureManager().bindTexture(vignetteTexPath);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, (double)par3, -90.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)par2, (double)par3, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)par2, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected void func_130015_b(float par1, int par2, int par3)
    {
        if (par1 < 1.0F)
        {
            par1 *= par1;
            par1 *= par1;
            par1 = par1 * 0.8F + 0.2F;
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, par1);
        Icon icon = Block.portal.getBlockTextureFromSide(1);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        float f1 = icon.getMinU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxU();
        float f4 = icon.getMaxV();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, (double)par3, -90.0D, (double)f1, (double)f4);
        tessellator.addVertexWithUV((double)par2, (double)par3, -90.0D, (double)f3, (double)f4);
        tessellator.addVertexWithUV((double)par2, 0.0D, -90.0D, (double)f3, (double)f2);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)f1, (double)f2);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick
     */
    protected void renderInventorySlot(int par1, int par2, int par3, float par4)
    {
        ItemStack itemstack = this.mc.thePlayer.inventory.mainInventory[par1];

        if (itemstack != null)
        {
            float f1 = (float)itemstack.animationsToGo - par4;

            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float)(par2 + 8), (float)(par3 + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(par2 + 8)), (float)(-(par3 + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, par2, par3);

            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, par2, par3);
        }
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick()
    {
        if (this.recordPlayingUpFor > 0)
        {
            --this.recordPlayingUpFor;
        }

        ++this.updateCounter;

        if (this.mc.thePlayer != null)
        {
            ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();

            if (itemstack == null)
            {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && itemstack.itemID == this.highlightingItemStack.itemID && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getItemDamage() == this.highlightingItemStack.getItemDamage()))
            {
                if (this.remainingHighlightTicks > 0)
                {
                    --this.remainingHighlightTicks;
                }
            }
            else
            {
                this.remainingHighlightTicks = 40;
            }

            this.highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        this.func_110326_a("Now playing: " + par1Str, true);
    }

    public void func_110326_a(String par1Str, boolean par2)
    {
        this.recordPlaying = par1Str;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = par2;
    }

    /**
     * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
     */
    public GuiNewChat getChatGUI()
    {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter()
    {
        return this.updateCounter;
    }
}
