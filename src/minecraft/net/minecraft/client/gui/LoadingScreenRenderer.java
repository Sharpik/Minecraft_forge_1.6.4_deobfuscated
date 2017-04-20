package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LoadingScreenRenderer implements IProgressUpdate
{
    private String field_73727_a = "";

    /** A reference to the Minecraft object. */
    private Minecraft mc;

    /**
     * The text currently displayed (i.e. the argument to the last call to printText or func_73722_d)
     */
    private String currentlyDisplayedText = "";
    private long field_73723_d = Minecraft.getSystemTime();
    private boolean field_73724_e;

    public LoadingScreenRenderer(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    public void resetProgressAndMessage(String par1Str)
    {
        this.field_73724_e = false;
        this.func_73722_d(par1Str);
    }

    /**
     * "Saving level", or the loading,or downloading equivelent
     */
    public void displayProgressMessage(String par1Str)
    {
        this.field_73724_e = true;
        this.func_73722_d(par1Str);
    }

    public void func_73722_d(String par1Str)
    {
        this.currentlyDisplayedText = par1Str;

        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        }
    }

    /**
     * This is called with "Working..." by resetProgressAndMessage
     */
    public void resetProgresAndWorkingMessage(String par1Str)
    {
        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            this.field_73723_d = 0L;
            this.field_73727_a = par1Str;
            this.setLoadingProgress(-1);
            this.field_73723_d = 0L;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    public void setLoadingProgress(int par1)
    {
        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            long j = Minecraft.getSystemTime();

            if (j - this.field_73723_d >= 100L)
            {
                this.field_73723_d = j;
                ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
                int k = scaledresolution.getScaledWidth();
                int l = scaledresolution.getScaledHeight();
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glTranslatef(0.0F, 0.0F, -200.0F);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                Tessellator tessellator = Tessellator.instance;
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float f = 32.0F;
                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(4210752);
                tessellator.addVertexWithUV(0.0D, (double)l, 0.0D, 0.0D, (double)((float)l / f));
                tessellator.addVertexWithUV((double)k, (double)l, 0.0D, (double)((float)k / f), (double)((float)l / f));
                tessellator.addVertexWithUV((double)k, 0.0D, 0.0D, (double)((float)k / f), 0.0D);
                tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                tessellator.draw();

                if (par1 >= 0)
                {
                    byte b0 = 100;
                    byte b1 = 2;
                    int i1 = k / 2 - b0 / 2;
                    int j1 = l / 2 + 16;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.setColorOpaque_I(8421504);
                    tessellator.addVertex((double)i1, (double)j1, 0.0D);
                    tessellator.addVertex((double)i1, (double)(j1 + b1), 0.0D);
                    tessellator.addVertex((double)(i1 + b0), (double)(j1 + b1), 0.0D);
                    tessellator.addVertex((double)(i1 + b0), (double)j1, 0.0D);
                    tessellator.setColorOpaque_I(8454016);
                    tessellator.addVertex((double)i1, (double)j1, 0.0D);
                    tessellator.addVertex((double)i1, (double)(j1 + b1), 0.0D);
                    tessellator.addVertex((double)(i1 + par1), (double)(j1 + b1), 0.0D);
                    tessellator.addVertex((double)(i1 + par1), (double)j1, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.mc.fontRenderer.drawStringWithShadow(this.currentlyDisplayedText, (k - this.mc.fontRenderer.getStringWidth(this.currentlyDisplayedText)) / 2, l / 2 - 4 - 16, 16777215);
                this.mc.fontRenderer.drawStringWithShadow(this.field_73727_a, (k - this.mc.fontRenderer.getStringWidth(this.field_73727_a)) / 2, l / 2 - 4 + 8, 16777215);
                Display.update();

                try
                {
                    Thread.yield();
                }
                catch (Exception exception)
                {
                    ;
                }
            }
        }
    }
}
