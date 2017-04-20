package net.minecraft.client.gui.achievement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;

@SideOnly(Side.CLIENT)
class GuiSlotStatsItem extends GuiSlotStats
{
    /** Instance of GuiStats. */
    final GuiStats slotGuiStats;

    public GuiSlotStatsItem(GuiStats par1GuiStats)
    {
        super(par1GuiStats);
        this.slotGuiStats = par1GuiStats;
        this.field_77266_h = new ArrayList();
        Iterator iterator = StatList.itemStats.iterator();

        while (iterator.hasNext())
        {
            StatCrafting statcrafting = (StatCrafting)iterator.next();
            boolean flag = false;
            int i = statcrafting.getItemID();

            if (GuiStats.getStatsFileWriter(par1GuiStats).writeStat(statcrafting) > 0)
            {
                flag = true;
            }
            else if (StatList.objectBreakStats[i] != null && GuiStats.getStatsFileWriter(par1GuiStats).writeStat(StatList.objectBreakStats[i]) > 0)
            {
                flag = true;
            }
            else if (StatList.objectCraftStats[i] != null && GuiStats.getStatsFileWriter(par1GuiStats).writeStat(StatList.objectCraftStats[i]) > 0)
            {
                flag = true;
            }

            if (flag)
            {
                this.field_77266_h.add(statcrafting);
            }
        }

        this.field_77267_i = new SorterStatsItem(this, par1GuiStats);
    }

    protected void func_77222_a(int par1, int par2, Tessellator par3Tessellator)
    {
        super.func_77222_a(par1, par2, par3Tessellator);

        if (this.field_77262_g == 0)
        {
            GuiStats.drawSprite(this.slotGuiStats, par1 + 115 - 18 + 1, par2 + 1 + 1, 72, 18);
        }
        else
        {
            GuiStats.drawSprite(this.slotGuiStats, par1 + 115 - 18, par2 + 1, 72, 18);
        }

        if (this.field_77262_g == 1)
        {
            GuiStats.drawSprite(this.slotGuiStats, par1 + 165 - 18 + 1, par2 + 1 + 1, 18, 18);
        }
        else
        {
            GuiStats.drawSprite(this.slotGuiStats, par1 + 165 - 18, par2 + 1, 18, 18);
        }

        if (this.field_77262_g == 2)
        {
            GuiStats.drawSprite(this.slotGuiStats, par1 + 215 - 18 + 1, par2 + 1 + 1, 36, 18);
        }
        else
        {
            GuiStats.drawSprite(this.slotGuiStats, par1 + 215 - 18, par2 + 1, 36, 18);
        }
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        StatCrafting statcrafting = this.func_77257_d(par1);
        int i1 = statcrafting.getItemID();
        GuiStats.drawItemSprite(this.slotGuiStats, par2 + 40, par3, i1);
        this.func_77260_a((StatCrafting)StatList.objectBreakStats[i1], par2 + 115, par3, par1 % 2 == 0);
        this.func_77260_a((StatCrafting)StatList.objectCraftStats[i1], par2 + 165, par3, par1 % 2 == 0);
        this.func_77260_a(statcrafting, par2 + 215, par3, par1 % 2 == 0);
    }

    protected String func_77258_c(int par1)
    {
        return par1 == 1 ? "stat.crafted" : (par1 == 2 ? "stat.used" : "stat.depleted");
    }
}
