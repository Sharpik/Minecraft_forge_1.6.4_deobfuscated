package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;

@SideOnly(Side.CLIENT)
class GuiSlotLanguage extends GuiSlot
{
    private final List field_77251_g;
    private final Map field_77253_h;

    final GuiLanguage languageGui;

    public GuiSlotLanguage(GuiLanguage par1GuiLanguage)
    {
        super(par1GuiLanguage.mc, par1GuiLanguage.width, par1GuiLanguage.height, 32, par1GuiLanguage.height - 65 + 4, 18);
        this.languageGui = par1GuiLanguage;
        this.field_77251_g = Lists.newArrayList();
        this.field_77253_h = Maps.newHashMap();
        Iterator iterator = GuiLanguage.func_135011_a(par1GuiLanguage).getLanguages().iterator();

        while (iterator.hasNext())
        {
            Language language = (Language)iterator.next();
            this.field_77253_h.put(language.getLanguageCode(), language);
            this.field_77251_g.add(language.getLanguageCode());
        }
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return this.field_77251_g.size();
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
        Language language = (Language)this.field_77253_h.get(this.field_77251_g.get(par1));
        GuiLanguage.func_135011_a(this.languageGui).setCurrentLanguage(language);
        GuiLanguage.getGameSettings(this.languageGui).language = language.getLanguageCode();
        this.languageGui.mc.refreshResources();
        this.languageGui.fontRenderer.setUnicodeFlag(GuiLanguage.func_135011_a(this.languageGui).isCurrentLocaleUnicode());
        this.languageGui.fontRenderer.setBidiFlag(GuiLanguage.func_135011_a(this.languageGui).isCurrentLanguageBidirectional());
        GuiLanguage.getDoneButton(this.languageGui).displayString = I18n.getString("gui.done");
        GuiLanguage.getGameSettings(this.languageGui).saveOptions();
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return ((String)this.field_77251_g.get(par1)).equals(GuiLanguage.func_135011_a(this.languageGui).getCurrentLanguage().getLanguageCode());
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * 18;
    }

    protected void drawBackground()
    {
        this.languageGui.drawDefaultBackground();
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        this.languageGui.fontRenderer.setBidiFlag(true);
        this.languageGui.drawCenteredString(this.languageGui.fontRenderer, ((Language)this.field_77253_h.get(this.field_77251_g.get(par1))).toString(), this.languageGui.width / 2, par3 + 1, 16777215);
        this.languageGui.fontRenderer.setBidiFlag(GuiLanguage.func_135011_a(this.languageGui).getCurrentLanguage().isBidirectional());
    }
}
