package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class SelectionListBase
{
    private final Minecraft mc;
    private final int field_96619_e;
    private final int field_96616_f;
    private final int field_96617_g;
    private final int field_96627_h;
    protected final int field_96620_b;
    protected int field_96621_c;
    protected int field_96618_d;
    private float field_96628_i = -2.0F;
    private float field_96625_j;
    private float field_96626_k;
    private int field_96623_l = -1;
    private long field_96624_m;

    public SelectionListBase(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6)
    {
        this.mc = par1Minecraft;
        this.field_96616_f = par3;
        this.field_96627_h = par3 + par5;
        this.field_96620_b = par6;
        this.field_96619_e = par2;
        this.field_96617_g = par2 + par4;
    }

    protected abstract int func_96608_a();

    protected abstract void func_96615_a(int i, boolean flag);

    protected abstract boolean func_96609_a(int i);

    protected int func_96613_b()
    {
        return this.func_96608_a() * this.field_96620_b;
    }

    protected abstract void func_96611_c();

    protected abstract void func_96610_a(int i, int j, int k, int l, Tessellator tessellator);

    private void func_96614_f()
    {
        int i = this.func_96607_d();

        if (i < 0)
        {
            i = 0;
        }

        if (this.field_96626_k < 0.0F)
        {
            this.field_96626_k = 0.0F;
        }

        if (this.field_96626_k > (float)i)
        {
            this.field_96626_k = (float)i;
        }
    }

    public int func_96607_d()
    {
        return this.func_96613_b() - (this.field_96627_h - this.field_96616_f - 4);
    }

    public void func_96612_a(int par1, int par2, float par3)
    {
        this.field_96621_c = par1;
        this.field_96618_d = par2;
        this.func_96611_c();
        int k = this.func_96608_a();
        int l = this.func_96606_e();
        int i1 = l + 6;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;

        if (Mouse.isButtonDown(0))
        {
            if (this.field_96628_i == -1.0F)
            {
                boolean flag = true;

                if (par2 >= this.field_96616_f && par2 <= this.field_96627_h)
                {
                    int k2 = this.field_96619_e + 2;
                    j1 = this.field_96617_g - 2;
                    k1 = par2 - this.field_96616_f + (int)this.field_96626_k - 4;
                    l1 = k1 / this.field_96620_b;

                    if (par1 >= k2 && par1 <= j1 && l1 >= 0 && k1 >= 0 && l1 < k)
                    {
                        boolean flag1 = l1 == this.field_96623_l && Minecraft.getSystemTime() - this.field_96624_m < 250L;
                        this.func_96615_a(l1, flag1);
                        this.field_96623_l = l1;
                        this.field_96624_m = Minecraft.getSystemTime();
                    }
                    else if (par1 >= k2 && par1 <= j1 && k1 < 0)
                    {
                        flag = false;
                    }

                    if (par1 >= l && par1 <= i1)
                    {
                        this.field_96625_j = -1.0F;
                        j2 = this.func_96607_d();

                        if (j2 < 1)
                        {
                            j2 = 1;
                        }

                        i2 = (int)((float)((this.field_96627_h - this.field_96616_f) * (this.field_96627_h - this.field_96616_f)) / (float)this.func_96613_b());

                        if (i2 < 32)
                        {
                            i2 = 32;
                        }

                        if (i2 > this.field_96627_h - this.field_96616_f - 8)
                        {
                            i2 = this.field_96627_h - this.field_96616_f - 8;
                        }

                        this.field_96625_j /= (float)(this.field_96627_h - this.field_96616_f - i2) / (float)j2;
                    }
                    else
                    {
                        this.field_96625_j = 1.0F;
                    }

                    if (flag)
                    {
                        this.field_96628_i = (float)par2;
                    }
                    else
                    {
                        this.field_96628_i = -2.0F;
                    }
                }
                else
                {
                    this.field_96628_i = -2.0F;
                }
            }
            else if (this.field_96628_i >= 0.0F)
            {
                this.field_96626_k -= ((float)par2 - this.field_96628_i) * this.field_96625_j;
                this.field_96628_i = (float)par2;
            }
        }
        else
        {
            while (!this.mc.gameSettings.touchscreen && Mouse.next())
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

                    this.field_96626_k += (float)(l2 * this.field_96620_b / 2);
                }
            }

            this.field_96628_i = -1.0F;
        }

        this.func_96614_f();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV((double)this.field_96619_e, (double)this.field_96627_h, 0.0D, (double)((float)this.field_96619_e / f1), (double)((float)(this.field_96627_h + (int)this.field_96626_k) / f1));
        tessellator.addVertexWithUV((double)this.field_96617_g, (double)this.field_96627_h, 0.0D, (double)((float)this.field_96617_g / f1), (double)((float)(this.field_96627_h + (int)this.field_96626_k) / f1));
        tessellator.addVertexWithUV((double)this.field_96617_g, (double)this.field_96616_f, 0.0D, (double)((float)this.field_96617_g / f1), (double)((float)(this.field_96616_f + (int)this.field_96626_k) / f1));
        tessellator.addVertexWithUV((double)this.field_96619_e, (double)this.field_96616_f, 0.0D, (double)((float)this.field_96619_e / f1), (double)((float)(this.field_96616_f + (int)this.field_96626_k) / f1));
        tessellator.draw();
        j1 = this.field_96619_e + 2;
        k1 = this.field_96616_f + 4 - (int)this.field_96626_k;
        int i3;

        for (l1 = 0; l1 < k; ++l1)
        {
            j2 = k1 + l1 * this.field_96620_b;
            i2 = this.field_96620_b - 4;

            if (j2 + this.field_96620_b <= this.field_96627_h && j2 - 4 >= this.field_96616_f)
            {
                if (this.func_96609_a(l1))
                {
                    i3 = this.field_96619_e + 2;
                    int j3 = this.field_96617_g - 2;
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

                this.func_96610_a(l1, j1, j2, i2, tessellator);
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte b0 = 4;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)this.field_96619_e, (double)(this.field_96616_f + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)this.field_96617_g, (double)(this.field_96616_f + b0), 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)this.field_96617_g, (double)this.field_96616_f, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)this.field_96619_e, (double)this.field_96616_f, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)this.field_96619_e, (double)this.field_96627_h, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)this.field_96617_g, (double)this.field_96627_h, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)this.field_96617_g, (double)(this.field_96627_h - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)this.field_96619_e, (double)(this.field_96627_h - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        j2 = this.func_96607_d();

        if (j2 > 0)
        {
            i2 = (this.field_96627_h - this.field_96616_f) * (this.field_96627_h - this.field_96616_f) / this.func_96613_b();

            if (i2 < 32)
            {
                i2 = 32;
            }

            if (i2 > this.field_96627_h - this.field_96616_f - 8)
            {
                i2 = this.field_96627_h - this.field_96616_f - 8;
            }

            i3 = (int)this.field_96626_k * (this.field_96627_h - this.field_96616_f - i2) / j2 + this.field_96616_f;

            if (i3 < this.field_96616_f)
            {
                i3 = this.field_96616_f;
            }

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV((double)l, (double)this.field_96627_h, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double)i1, (double)this.field_96627_h, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double)i1, (double)this.field_96616_f, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double)l, (double)this.field_96616_f, 0.0D, 0.0D, 0.0D);
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

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected int func_96606_e()
    {
        return this.field_96617_g - 8;
    }
}
