package net.minecraft.client.gui.achievement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiAchievement extends Gui
{
    private static final ResourceLocation achievementTextures = new ResourceLocation("textures/gui/achievement/achievement_background.png");

    /** Holds the instance of the game (Minecraft) */
    private Minecraft theGame;

    /** Holds the latest width scaled to fit the game window. */
    private int achievementWindowWidth;

    /** Holds the latest height scaled to fit the game window. */
    private int achievementWindowHeight;
    private String achievementGetLocalText;
    private String achievementStatName;

    /** Holds the achievement that will be displayed on the GUI. */
    private Achievement theAchievement;
    private long achievementTime;

    /**
     * Holds a instance of RenderItem, used to draw the achievement icons on screen (is based on ItemStack)
     */
    private RenderItem itemRender;
    private boolean haveAchiement;

    public GuiAchievement(Minecraft par1Minecraft)
    {
        this.theGame = par1Minecraft;
        this.itemRender = new RenderItem();
    }

    /**
     * Queue a taken achievement to be displayed.
     */
    public void queueTakenAchievement(Achievement par1Achievement)
    {
        this.achievementGetLocalText = I18n.getString("achievement.get");
        this.achievementStatName = I18n.getString(par1Achievement.getName());
        this.achievementTime = Minecraft.getSystemTime();
        this.theAchievement = par1Achievement;
        this.haveAchiement = false;
    }

    /**
     * Queue a information about a achievement to be displayed.
     */
    public void queueAchievementInformation(Achievement par1Achievement)
    {
        this.achievementGetLocalText = I18n.getString(par1Achievement.getName());
        this.achievementStatName = par1Achievement.getDescription();
        this.achievementTime = Minecraft.getSystemTime() - 2500L;
        this.theAchievement = par1Achievement;
        this.haveAchiement = true;
    }

    /**
     * Update the display of the achievement window to match the game window.
     */
    private void updateAchievementWindowScale()
    {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.achievementWindowWidth = this.theGame.displayWidth;
        this.achievementWindowHeight = this.theGame.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.achievementWindowWidth = scaledresolution.getScaledWidth();
        this.achievementWindowHeight = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.achievementWindowWidth, (double)this.achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    /**
     * Updates the small achievement tooltip window, showing a queued achievement if is needed.
     */
    public void updateAchievementWindow()
    {
        if (this.theAchievement != null && this.achievementTime != 0L)
        {
            double d0 = (double)(Minecraft.getSystemTime() - this.achievementTime) / 3000.0D;

            if (!this.haveAchiement && (d0 < 0.0D || d0 > 1.0D))
            {
                this.achievementTime = 0L;
            }
            else
            {
                this.updateAchievementWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                double d1 = d0 * 2.0D;

                if (d1 > 1.0D)
                {
                    d1 = 2.0D - d1;
                }

                d1 *= 4.0D;
                d1 = 1.0D - d1;

                if (d1 < 0.0D)
                {
                    d1 = 0.0D;
                }

                d1 *= d1;
                d1 *= d1;
                int i = this.achievementWindowWidth - 160;
                int j = 0 - (int)(d1 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.theGame.getTextureManager().bindTexture(achievementTextures);
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

                if (this.haveAchiement)
                {
                    this.theGame.fontRenderer.drawSplitString(this.achievementStatName, i + 30, j + 7, 120, -1);
                }
                else
                {
                    this.theGame.fontRenderer.drawString(this.achievementGetLocalText, i + 30, j + 7, -256);
                    this.theGame.fontRenderer.drawString(this.achievementStatName, i + 30, j + 18, -1);
                }

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                this.itemRender.renderItemAndEffectIntoGUI(this.theGame.fontRenderer, this.theGame.getTextureManager(), this.theAchievement.theItemStack, i + 8, j + 8);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
