package net.minecraft.client.gui.achievement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.stats.StatCrafting;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
abstract class GuiSlotStats extends GuiSlot
{
    protected int field_77262_g;
    protected List field_77266_h;
    protected Comparator field_77267_i;
    protected int field_77264_j;
    protected int field_77265_k;

    final GuiStats statsGui;

    protected GuiSlotStats(GuiStats par1GuiStats)
    {
        super(GuiStats.getMinecraft1(par1GuiStats), par1GuiStats.width, par1GuiStats.height, 32, par1GuiStats.height - 64, 20);
        this.statsGui = par1GuiStats;
        this.field_77262_g = -1;
        this.field_77264_j = -1;
        this.setShowSelectionBox(false);
        this.func_77223_a(true, 20);
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2) {}

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return false;
    }

    protected void drawBackground()
    {
        this.statsGui.drawDefaultBackground();
    }

    protected void func_77222_a(int par1, int par2, Tessellator par3Tessellator)
    {
        if (!Mouse.isButtonDown(0))
        {
            this.field_77262_g = -1;
        }

        if (this.field_77262_g == 0)
        {
            GuiStats.drawSprite(this.statsGui, par1 + 115 - 18, par2 + 1, 0, 0);
        }
        else
        {
            GuiStats.drawSprite(this.statsGui, par1 + 115 - 18, par2 + 1, 0, 18);
        }

        if (this.field_77262_g == 1)
        {
            GuiStats.drawSprite(this.statsGui, par1 + 165 - 18, par2 + 1, 0, 0);
        }
        else
        {
            GuiStats.drawSprite(this.statsGui, par1 + 165 - 18, par2 + 1, 0, 18);
        }

        if (this.field_77262_g == 2)
        {
            GuiStats.drawSprite(this.statsGui, par1 + 215 - 18, par2 + 1, 0, 0);
        }
        else
        {
            GuiStats.drawSprite(this.statsGui, par1 + 215 - 18, par2 + 1, 0, 18);
        }

        if (this.field_77264_j != -1)
        {
            short short1 = 79;
            byte b0 = 18;

            if (this.field_77264_j == 1)
            {
                short1 = 129;
            }
            else if (this.field_77264_j == 2)
            {
                short1 = 179;
            }

            if (this.field_77265_k == 1)
            {
                b0 = 36;
            }

            GuiStats.drawSprite(this.statsGui, par1 + short1, par2 + 1, b0, 0);
        }
    }

    protected void func_77224_a(int par1, int par2)
    {
        this.field_77262_g = -1;

        if (par1 >= 79 && par1 < 115)
        {
            this.field_77262_g = 0;
        }
        else if (par1 >= 129 && par1 < 165)
        {
            this.field_77262_g = 1;
        }
        else if (par1 >= 179 && par1 < 215)
        {
            this.field_77262_g = 2;
        }

        if (this.field_77262_g >= 0)
        {
            this.func_77261_e(this.field_77262_g);
            GuiStats.getMinecraft2(this.statsGui).sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        }
    }

    /**
     * Gets the size of the current slot list.
     */
    protected final int getSize()
    {
        return this.field_77266_h.size();
    }

    protected final StatCrafting func_77257_d(int par1)
    {
        return (StatCrafting)this.field_77266_h.get(par1);
    }

    protected abstract String func_77258_c(int i);

    protected void func_77260_a(StatCrafting par1StatCrafting, int par2, int par3, boolean par4)
    {
        String s;

        if (par1StatCrafting != null)
        {
            s = par1StatCrafting.func_75968_a(GuiStats.getStatsFileWriter(this.statsGui).writeStat(par1StatCrafting));
            this.statsGui.drawString(GuiStats.getFontRenderer4(this.statsGui), s, par2 - GuiStats.getFontRenderer5(this.statsGui).getStringWidth(s), par3 + 5, par4 ? 16777215 : 9474192);
        }
        else
        {
            s = "-";
            this.statsGui.drawString(GuiStats.getFontRenderer6(this.statsGui), s, par2 - GuiStats.getFontRenderer7(this.statsGui).getStringWidth(s), par3 + 5, par4 ? 16777215 : 9474192);
        }
    }

    protected void func_77215_b(int par1, int par2)
    {
        if (par2 >= this.top && par2 <= this.bottom)
        {
            int k = this.func_77210_c(par1, par2);
            int l = this.statsGui.width / 2 - 92 - 16;

            if (k >= 0)
            {
                if (par1 < l + 40 || par1 > l + 40 + 20)
                {
                    return;
                }

                StatCrafting statcrafting = this.func_77257_d(k);
                this.func_77259_a(statcrafting, par1, par2);
            }
            else
            {
                String s = "";

                if (par1 >= l + 115 - 18 && par1 <= l + 115)
                {
                    s = this.func_77258_c(0);
                }
                else if (par1 >= l + 165 - 18 && par1 <= l + 165)
                {
                    s = this.func_77258_c(1);
                }
                else
                {
                    if (par1 < l + 215 - 18 || par1 > l + 215)
                    {
                        return;
                    }

                    s = this.func_77258_c(2);
                }

                s = ("" + I18n.getString(s)).trim();

                if (s.length() > 0)
                {
                    int i1 = par1 + 12;
                    int j1 = par2 - 12;
                    int k1 = GuiStats.getFontRenderer8(this.statsGui).getStringWidth(s);
                    GuiStats.drawGradientRect(this.statsGui, i1 - 3, j1 - 3, i1 + k1 + 3, j1 + 8 + 3, -1073741824, -1073741824);
                    GuiStats.getFontRenderer9(this.statsGui).drawStringWithShadow(s, i1, j1, -1);
                }
            }
        }
    }

    protected void func_77259_a(StatCrafting par1StatCrafting, int par2, int par3)
    {
        if (par1StatCrafting != null)
        {
            Item item = Item.itemsList[par1StatCrafting.getItemID()];
            String s = ("" + I18n.getString(item.getUnlocalizedName() + ".name")).trim();

            if (s.length() > 0)
            {
                int k = par2 + 12;
                int l = par3 - 12;
                int i1 = GuiStats.getFontRenderer10(this.statsGui).getStringWidth(s);
                GuiStats.drawGradientRect1(this.statsGui, k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
                GuiStats.getFontRenderer11(this.statsGui).drawStringWithShadow(s, k, l, -1);
            }
        }
    }

    protected void func_77261_e(int par1)
    {
        if (par1 != this.field_77264_j)
        {
            this.field_77264_j = par1;
            this.field_77265_k = -1;
        }
        else if (this.field_77265_k == -1)
        {
            this.field_77265_k = 1;
        }
        else
        {
            this.field_77264_j = -1;
            this.field_77265_k = 0;
        }

        Collections.sort(this.field_77266_h, this.field_77267_i);
    }
}
