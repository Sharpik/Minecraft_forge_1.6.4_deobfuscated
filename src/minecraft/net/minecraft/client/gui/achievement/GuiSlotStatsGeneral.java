package net.minecraft.client.gui.achievement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

@SideOnly(Side.CLIENT)
class GuiSlotStatsGeneral extends GuiSlot
{
    final GuiStats statsGui;

    public GuiSlotStatsGeneral(GuiStats par1GuiStats)
    {
        super(GuiStats.getMinecraft(par1GuiStats), par1GuiStats.width, par1GuiStats.height, 32, par1GuiStats.height - 64, 10);
        this.statsGui = par1GuiStats;
        this.setShowSelectionBox(false);
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return StatList.generalStats.size();
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

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * 10;
    }

    protected void drawBackground()
    {
        this.statsGui.drawDefaultBackground();
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        StatBase statbase = (StatBase)StatList.generalStats.get(par1);
        this.statsGui.drawString(GuiStats.getFontRenderer1(this.statsGui), I18n.getString(statbase.getName()), par2 + 2, par3 + 1, par1 % 2 == 0 ? 16777215 : 9474192);
        String s = statbase.func_75968_a(GuiStats.getStatsFileWriter(this.statsGui).writeStat(statbase));
        this.statsGui.drawString(GuiStats.getFontRenderer2(this.statsGui), s, par2 + 2 + 213 - GuiStats.getFontRenderer3(this.statsGui).getStringWidth(s), par3 + 1, par1 % 2 == 0 ? 16777215 : 9474192);
    }
}
