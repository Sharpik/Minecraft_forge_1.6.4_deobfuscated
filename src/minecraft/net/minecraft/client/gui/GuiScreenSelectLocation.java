package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class GuiScreenSelectLocation
{
    private final Minecraft field_104092_f;
    private int field_104093_g;
    private int field_104105_h;
    protected int field_104098_a;
    protected int field_104096_b;
    private int field_104106_i;
    private int field_104103_j;
    protected final int field_104097_c;
    private int field_104104_k;
    private int field_104101_l;
    protected int field_104094_d;
    protected int field_104095_e;
    private float field_104102_m = -2.0F;
    private float field_104099_n;
    private float field_104100_o;
    private int field_104111_p = -1;
    private long field_104110_q;
    private boolean field_104109_r = true;
    private boolean field_104108_s;
    private int field_104107_t;

    public GuiScreenSelectLocation(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6)
    {
        this.field_104092_f = par1Minecraft;
        this.field_104093_g = par2;
        this.field_104105_h = par3;
        this.field_104098_a = par4;
        this.field_104096_b = par5;
        this.field_104097_c = par6;
        this.field_104103_j = 0;
        this.field_104106_i = par2;
    }

    public void func_104084_a(int par1, int par2, int par3, int par4)
    {
        this.field_104093_g = par1;
        this.field_104105_h = par2;
        this.field_104098_a = par3;
        this.field_104096_b = par4;
        this.field_104103_j = 0;
        this.field_104106_i = par1;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected abstract int getSize();

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected abstract void elementClicked(int i, boolean flag);

    /**
     * returns true if the element passed in is currently selected
     */
    protected abstract boolean isSelected(int i);

    protected abstract boolean func_104086_b(int i);

    protected int func_130003_b()
    {
        return this.getSize() * this.field_104097_c + this.field_104107_t;
    }

    protected abstract void func_130004_c();

    protected abstract void drawSlot(int i, int j, int k, int l, Tessellator tessellator);

    protected void func_104088_a(int par1, int par2, Tessellator par3Tessellator) {}

    protected void func_104089_a(int par1, int par2) {}

    protected void func_104087_b(int par1, int par2) {}

    private void func_104091_h()
    {
        int i = this.func_104085_d();

        if (i < 0)
        {
            i /= 2;
        }

        if (this.field_104100_o < 0.0F)
        {
            this.field_104100_o = 0.0F;
        }

        if (this.field_104100_o > (float)i)
        {
            this.field_104100_o = (float)i;
        }
    }

    public int func_104085_d()
    {
        return this.func_130003_b() - (this.field_104096_b - this.field_104098_a - 4);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    public void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == this.field_104104_k)
            {
                this.field_104100_o -= (float)(this.field_104097_c * 2 / 3);
                this.field_104102_m = -2.0F;
                this.func_104091_h();
            }
            else if (par1GuiButton.id == this.field_104101_l)
            {
                this.field_104100_o += (float)(this.field_104097_c * 2 / 3);
                this.field_104102_m = -2.0F;
                this.func_104091_h();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.field_104094_d = par1;
        this.field_104095_e = par2;
        this.func_130004_c();
        int k = this.getSize();
        int l = this.func_104090_g();
        int i1 = l + 6;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;

        if (Mouse.isButtonDown(0))
        {
            if (this.field_104102_m == -1.0F)
            {
                boolean flag = true;

                if (par2 >= this.field_104098_a && par2 <= this.field_104096_b)
                {
                    int k2 = this.field_104093_g / 2 - 110;
                    j1 = this.field_104093_g / 2 + 110;
                    k1 = par2 - this.field_104098_a - this.field_104107_t + (int)this.field_104100_o - 4;
                    l1 = k1 / this.field_104097_c;

                    if (par1 >= k2 && par1 <= j1 && l1 >= 0 && k1 >= 0 && l1 < k)
                    {
                        boolean flag1 = l1 == this.field_104111_p && Minecraft.getSystemTime() - this.field_104110_q < 250L;
                        this.elementClicked(l1, flag1);
                        this.field_104111_p = l1;
                        this.field_104110_q = Minecraft.getSystemTime();
                    }
                    else if (par1 >= k2 && par1 <= j1 && k1 < 0)
                    {
                        this.func_104089_a(par1 - k2, par2 - this.field_104098_a + (int)this.field_104100_o - 4);
                        flag = false;
                    }

                    if (par1 >= l && par1 <= i1)
                    {
                        this.field_104099_n = -1.0F;
                        j2 = this.func_104085_d();

                        if (j2 < 1)
                        {
                            j2 = 1;
                        }

                        i2 = (int)((float)((this.field_104096_b - this.field_104098_a) * (this.field_104096_b - this.field_104098_a)) / (float)this.func_130003_b());

                        if (i2 < 32)
                        {
                            i2 = 32;
                        }

                        if (i2 > this.field_104096_b - this.field_104098_a - 8)
                        {
                            i2 = this.field_104096_b - this.field_104098_a - 8;
                        }

                        this.field_104099_n /= (float)(this.field_104096_b - this.field_104098_a - i2) / (float)j2;
                    }
                    else
                    {
                        this.field_104099_n = 1.0F;
                    }

                    if (flag)
                    {
                        this.field_104102_m = (float)par2;
                    }
                    else
                    {
                        this.field_104102_m = -2.0F;
                    }
                }
                else
                {
                    this.field_104102_m = -2.0F;
                }
            }
            else if (this.field_104102_m >= 0.0F)
            {
                this.field_104100_o -= ((float)par2 - this.field_104102_m) * this.field_104099_n;
                this.field_104102_m = (float)par2;
            }
        }
        else
        {
            while (!this.field_104092_f.gameSettings.touchscreen && Mouse.next())
            {
                int l2 = Mouse.getEventDWheel();

                if (l2 != 0)
                {
                    if (l2 > 0)
                    {
                        l2 = -1;
                    }
                    else if (l2 < 0)
                    {
                        l2 = 1;
                    }

                    this.field_104100_o += (float)(l2 * this.field_104097_c / 2);
                }
            }

            this.field_104102_m = -1.0F;
        }

        this.func_104091_h();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.field_104092_f.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV((double)this.field_104103_j, (double)this.field_104096_b, 0.0D, (double)((float)this.field_104103_j / f1), (double)((float)(this.field_104096_b + (int)this.field_104100_o) / f1));
        tessellator.addVertexWithUV((double)this.field_104106_i, (double)this.field_104096_b, 0.0D, (double)((float)this.field_104106_i / f1), (double)((float)(this.field_104096_b + (int)this.field_104100_o) / f1));
        tessellator.addVertexWithUV((double)this.field_104106_i, (double)this.field_104098_a, 0.0D, (double)((float)this.field_104106_i / f1), (double)((float)(this.field_104098_a + (int)this.field_104100_o) / f1));
        tessellator.addVertexWithUV((double)this.field_104103_j, (double)this.field_104098_a, 0.0D, (double)((float)this.field_104103_j / f1), (double)((float)(this.field_104098_a + (int)this.field_104100_o) / f1));
        tessellator.draw();
        j1 = this.field_104093_g / 2 - 92 - 16;
        k1 = this.field_104098_a + 4 - (int)this.field_104100_o;

        if (this.field_104108_s)
        {
            this.func_104088_a(j1, k1, tessellator);
        }

        int i3;

        for (l1 = 0; l1 < k; ++l1)
        {
            j2 = k1 + l1 * this.field_104097_c + this.field_104107_t;
            i2 = this.field_104097_c - 4;

            if (j2 <= this.field_104096_b && j2 + i2 >= this.field_104098_a)
            {
                int j3;

                if (this.field_104109_r && this.func_104086_b(l1))
                {
                    i3 = this.field_104093_g / 2 - 110;
                    j3 = this.field_104093_g / 2 + 110;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.setColorOpaque_I(0);
                    tessellator.addVertexWithUV((double)i3, (double)(j2 + i2 + 2), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double)j3, (double)(j2 + i2 + 2), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double)j3, (double)(j2 - 2), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double)i3, (double)(j2 - 2), 0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                if (this.field_104109_r && this.isSelected(l1))
                {
                    i3 = this.field_104093_g / 2 - 110;
                    j3 = this.field_104093_g / 2 + 110;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.setColorOpaque_I(8421504);
                    tessellator.addVertexWithUV((double)i3, (double)(j2 + i2 + 2), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double)j3, (double)(j2 + i2 + 2), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double)j3, (double)(j2 - 2), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double)i3, (double)(j2 - 2), 0.0D, 0.0D, 0.0D);
                    tessellator.setColorOpaque_I(0);
                    tessellator.addVertexWithUV((double)(i3 + 1), (double)(j2 + i2 + 1), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double)(j3 - 1), (double)(j2 + i2 + 1), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double)(j3 - 1), (double)(j2 - 1), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double)(i3 + 1), (double)(j2 - 1), 0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.drawSlot(l1, j1, j2, i2, tessellator);
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte b0 = 4;
        this.func_104083_b(0, this.field_104098_a, 255, 255);
        this.func_104083_b(this.field_104096_b, this.field_104105_h, 255, 255);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)this.field_104103_j, (double)(this.field_104098_a + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)this.field_104106_i, (double)(this.field_104098_a + b0), 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)this.field_104106_i, (double)this.field_104098_a, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)this.field_104103_j, (double)this.field_104098_a, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)this.field_104103_j, (double)this.field_104096_b, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)this.field_104106_i, (double)this.field_104096_b, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)this.field_104106_i, (double)(this.field_104096_b - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)this.field_104103_j, (double)(this.field_104096_b - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        j2 = this.func_104085_d();

        if (j2 > 0)
        {
            i2 = (this.field_104096_b - this.field_104098_a) * (this.field_104096_b - this.field_104098_a) / this.func_130003_b();

            if (i2 < 32)
            {
                i2 = 32;
            }

            if (i2 > this.field_104096_b - this.field_104098_a - 8)
            {
                i2 = this.field_104096_b - this.field_104098_a - 8;
            }

            i3 = (int)this.field_104100_o * (this.field_104096_b - this.field_104098_a - i2) / j2 + this.field_104098_a;

            if (i3 < this.field_104098_a)
            {
                i3 = this.field_104098_a;
            }

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV((double)l, (double)this.field_104096_b, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)i1, (double)this.field_104096_b, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)i1, (double)this.field_104098_a, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double)l, (double)this.field_104098_a, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV((double)l, (double)(i3 + i2), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)i1, (double)(i3 + i2), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)i1, (double)i3, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double)l, (double)i3, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV((double)l, (double)(i3 + i2 - 1), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)(i1 - 1), (double)(i3 + i2 - 1), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)(i1 - 1), (double)i3, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double)l, (double)i3, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }

        this.func_104087_b(par1, par2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected int func_104090_g()
    {
        return this.field_104093_g / 2 + 124;
    }

    private void func_104083_b(int par1, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        this.field_104092_f.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(4210752, par4);
        tessellator.addVertexWithUV(0.0D, (double)par2, 0.0D, 0.0D, (double)((float)par2 / f));
        tessellator.addVertexWithUV((double)this.field_104093_g, (double)par2, 0.0D, (double)((float)this.field_104093_g / f), (double)((float)par2 / f));
        tessellator.setColorRGBA_I(4210752, par3);
        tessellator.addVertexWithUV((double)this.field_104093_g, (double)par1, 0.0D, (double)((float)this.field_104093_g / f), (double)((float)par1 / f));
        tessellator.addVertexWithUV(0.0D, (double)par1, 0.0D, 0.0D, (double)((float)par1 / f));
        tessellator.draw();
    }
}
