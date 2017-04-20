package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.network.packet.Packet205ClientCommand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiWinGame extends GuiScreen
{
    private static final ResourceLocation minecraftLogoTexture = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation field_110361_b = new ResourceLocation("textures/misc/vignette.png");

    /** Counts the number of screen updates. */
    private int updateCounter;

    /** List of lines on the ending poem and credits. */
    private List lines;
    private int field_73989_c;
    private float field_73987_d = 0.5F;

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.updateCounter;
        float f = (float)(this.field_73989_c + this.height + this.height + 24) / this.field_73987_d;

        if ((float)this.updateCounter > f)
        {
            this.respawnPlayer();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            this.respawnPlayer();
        }
    }

    /**
     * Respawns the player.
     */
    private void respawnPlayer()
    {
        this.mc.thePlayer.sendQueue.addToSendQueue(new Packet205ClientCommand(1));
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        if (this.lines == null)
        {
            this.lines = new ArrayList();

            try
            {
                String s = "";
                String s1 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
                short short1 = 274;
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(), Charsets.UTF_8));
                Random random = new Random(8124371L);
                int i;

                while ((s = bufferedreader.readLine()) != null)
                {
                    String s2;
                    String s3;

                    for (s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); s.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3)
                    {
                        i = s.indexOf(s1);
                        s2 = s.substring(0, i);
                        s3 = s.substring(i + s1.length());
                    }

                    this.lines.addAll(this.mc.fontRenderer.listFormattedStringToWidth(s, short1));
                    this.lines.add("");
                }

                for (i = 0; i < 8; ++i)
                {
                    this.lines.add("");
                }

                bufferedreader = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream(), Charsets.UTF_8));

                while ((s = bufferedreader.readLine()) != null)
                {
                    s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    s = s.replaceAll("\t", "    ");
                    this.lines.addAll(this.mc.fontRenderer.listFormattedStringToWidth(s, short1));
                    this.lines.add("");
                }

                this.field_73989_c = this.lines.size() * 12;
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    private void func_73986_b(int par1, int par2, float par3)
    {
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        float f1 = 0.0F - ((float)this.updateCounter + par3) * 0.5F * this.field_73987_d;
        float f2 = (float)this.height - ((float)this.updateCounter + par3) * 0.5F * this.field_73987_d;
        float f3 = 0.015625F;
        float f4 = ((float)this.updateCounter + par3 - 0.0F) * 0.02F;
        float f5 = (float)(this.field_73989_c + this.height + this.height + 24) / this.field_73987_d;
        float f6 = (f5 - 20.0F - ((float)this.updateCounter + par3)) * 0.005F;

        if (f6 < f4)
        {
            f4 = f6;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        f4 *= f4;
        f4 = f4 * 96.0F / 255.0F;
        tessellator.setColorOpaque_F(f4, f4, f4);
        tessellator.addVertexWithUV(0.0D, (double)this.height, (double)this.zLevel, 0.0D, (double)(f1 * f3));
        tessellator.addVertexWithUV((double)k, (double)this.height, (double)this.zLevel, (double)((float)k * f3), (double)(f1 * f3));
        tessellator.addVertexWithUV((double)k, 0.0D, (double)this.zLevel, (double)((float)k * f3), (double)(f2 * f3));
        tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, (double)(f2 * f3));
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.func_73986_b(par1, par2, par3);
        Tessellator tessellator = Tessellator.instance;
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        int l = this.height + 50;
        float f1 = -((float)this.updateCounter + par3) * this.field_73987_d;
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, f1, 0.0F);
        this.mc.getTextureManager().bindTexture(minecraftLogoTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(k, l, 0, 0, 155, 44);
        this.drawTexturedModalRect(k + 155, l, 0, 45, 155, 44);
        tessellator.setColorOpaque_I(16777215);
        int i1 = l + 200;
        int j1;

        for (j1 = 0; j1 < this.lines.size(); ++j1)
        {
            if (j1 == this.lines.size() - 1)
            {
                float f2 = (float)i1 + f1 - (float)(this.height / 2 - 6);

                if (f2 < 0.0F)
                {
                    GL11.glTranslatef(0.0F, -f2, 0.0F);
                }
            }

            if ((float)i1 + f1 + 12.0F + 8.0F > 0.0F && (float)i1 + f1 < (float)this.height)
            {
                String s = (String)this.lines.get(j1);

                if (s.startsWith("[C]"))
                {
                    this.fontRenderer.drawStringWithShadow(s.substring(3), k + (short1 - this.fontRenderer.getStringWidth(s.substring(3))) / 2, i1, 16777215);
                }
                else
                {
                    this.fontRenderer.fontRandom.setSeed((long)j1 * 4238972211L + (long)(this.updateCounter / 4));
                    this.fontRenderer.drawStringWithShadow(s, k, i1, 16777215);
                }
            }

            i1 += 12;
        }

        GL11.glPopMatrix();
        this.mc.getTextureManager().bindTexture(field_110361_b);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        j1 = this.width;
        int k1 = this.height;
        tessellator.addVertexWithUV(0.0D, (double)k1, (double)this.zLevel, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)j1, (double)k1, (double)this.zLevel, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)j1, 0.0D, (double)this.zLevel, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        super.drawScreen(par1, par2, par3);
    }
}
